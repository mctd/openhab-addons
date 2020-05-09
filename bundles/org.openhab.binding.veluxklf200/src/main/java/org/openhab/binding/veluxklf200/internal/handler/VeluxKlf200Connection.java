package org.openhab.binding.veluxklf200.internal.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200Helpers;
import org.openhab.binding.veluxklf200.internal.commands.request.BaseRequest;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_PROTOCOL_VERSION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_STATE_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_VERSION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_HOUSE_STATUS_MONITOR_ENABLE_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_PASSWORD_ENTER_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_SET_UTC_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseConfirmationResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_PROTOCOL_VERSION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_STATE_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_VERSION_CFM;
import org.openhab.binding.veluxklf200.internal.events.BaseEvent;
import org.openhab.binding.veluxklf200.internal.events.EventBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class VeluxKlf200Connection implements Runnable {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final static int COMMAND_ACK_TIMEOUT = 10 * 1000; // milliseconds
    private final static int SOCKET_CONNECT_TIMEOUT = 10 * 1000; // milliseconds
    private final static int SOCKET_READ_TIMEOUT = 10 * 1000; // milliseconds

    private @Nullable ScheduledFuture<?> receiverJob;
    private @Nullable ScheduledFuture<?> keepAliveJob;

    private VeluxKlf200BridgeHandler bridgeHandler;

    private @Nullable DataOutputStream outputStream = null;
    private @Nullable BaseRequest<?> runningCommand = null;
    private String hostname;
    private String password;
    private int port;
    private int keepaliveInterval;
    private @Nullable Socket klfSocket;
    private ScheduledExecutorService scheduler;

    public VeluxKlf200Connection(VeluxKlf200BridgeHandler bridgeHandler, ScheduledExecutorService scheduler) {
        this.bridgeHandler = bridgeHandler;
        this.hostname = bridgeHandler.getConfiguration().hostname;
        this.port = bridgeHandler.getConfiguration().port;
        this.password = bridgeHandler.getConfiguration().password;
        this.keepaliveInterval = bridgeHandler.getConfiguration().keepalive;
        this.scheduler = scheduler;
    }

    public ThingUID getBridgeUID() {
        return this.bridgeHandler.getThing().getUID();
    }

    public synchronized void sendRequest(BaseRequest<?> request) {
        logger.trace("Executing: {}", request.toString());

        // This protection might be useless as method is synchronized but already saw something wrong (cos we call
        // wait() later on?)
        // TODO : implement a true lock ?
        if (this.runningCommand != null) {
            logger.error(
                    "Trying to execute a command while another one did not finish yet. Running command: {}, asked: {}",
                    this.runningCommand.toString(), request.toString());
            return;
        }

        DataOutputStream localOutputStream = this.outputStream;
        if (localOutputStream == null) {
            logger.warn("Unable to execute command {}, connection is down.", request);
            return;
        }

        this.runningCommand = request;
        byte[] slipFrame = request.getSlipFrame();
        logger.trace("Sending: {}", VeluxKlf200Helpers.byteArrayToHexString(slipFrame));
        try {
            localOutputStream.write(slipFrame);
            localOutputStream.flush();
        } catch (IOException e) {
            logger.error("Unable to write to socket: {}", e.getMessage());
            // Closes everything and set socket to null. This will be detected by periodic check.
            this.runningCommand = null;
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(this.klfSocket);
            this.klfSocket = null;
        }

        synchronized (request) {
            try {
                request.wait(COMMAND_ACK_TIMEOUT);
            } catch (InterruptedException e) {
                logger.warn("Wait for request completion interrupted.");
            }
        }

        if (request.getResponse() == null) {
            logger.error("Request was not acknowledged during specified time.");
        }

        this.runningCommand = null;

        logger.trace("Execute complete: {}", request.toString());
    }

    @Override
    public void run() {
        logger.debug("Starting connection manager job.");

        // Start the infinite job
        runForever();

        // Infinity is over, cleanup before task end.
        this.dispose();
    }

    private void runForever() {
        Socket klfSocket;
        try {
            // Connect an SSL socket to KLF 200
            klfSocket = this.connect(this.hostname, this.port);
            this.klfSocket = klfSocket;
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Error connecting to KLF 200: {}", e.getMessage());
            this.bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            return;
        }

        logger.info("SSL connection to the KLF unit {} successfully established on port {}.", this.hostname, this.port);

        try {
            this.outputStream = new DataOutputStream(klfSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("Error getting output stream: {}", e.getMessage());
            return;
        }

        // Start the receiver thread
        startReceiverJob();

        // Login to KLF200
        GW_PASSWORD_ENTER_REQ loginRequest = new GW_PASSWORD_ENTER_REQ(this.password);

        this.sendRequest(loginRequest);
        if (!loginRequest.isLoggedIn()) {
            logger.error("Login failed");
            this.bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Unable to login (bad password?)");
            return;
        }

        logger.debug("Login successful");

        // Set the unit UTC time
        GW_SET_UTC_REQ setTimeRequest = new GW_SET_UTC_REQ();
        this.sendRequest(setTimeRequest);

        // Get and update the KLF200 properties
        String softwareVersion = null, hardwareVersion = null, productGroup = null, productType = null, state = null,
                protocol = null;

        GW_GET_VERSION_REQ getVersionReq = new GW_GET_VERSION_REQ();
        this.sendRequest(getVersionReq);
        GW_GET_VERSION_CFM getVersionResponse = getVersionReq.getResponse();
        if (getVersionResponse != null) {
            softwareVersion = getVersionResponse.getSofwareVersion();
            hardwareVersion = String.valueOf(getVersionResponse.getHardwareVersion());
            productGroup = getVersionResponse.getProductGroup().toString();
            productType = getVersionResponse.getProductType().toString();
        }

        GW_GET_STATE_REQ getStateReq = new GW_GET_STATE_REQ();
        this.sendRequest(getStateReq);
        GW_GET_STATE_CFM getStateResponse = getStateReq.getResponse();
        if (getStateResponse != null) {
            state = String.format("%s - %s", getStateResponse.getGatewayState(), getStateResponse.getGatewaySubState());
        }

        GW_GET_PROTOCOL_VERSION_REQ getProtocolVersionReq = new GW_GET_PROTOCOL_VERSION_REQ();
        this.sendRequest(getProtocolVersionReq);
        GW_GET_PROTOCOL_VERSION_CFM getProtocolVersionResponse = getProtocolVersionReq.getResponse();
        if (getProtocolVersionResponse != null) {
            protocol = getProtocolVersionResponse.getFullVersion();
        }

        this.bridgeHandler.updateProperties(softwareVersion, hardwareVersion, productGroup, productType, protocol,
                state);

        // Enable House Status Monitor (unit will send periodic status of nodes)
        GW_HOUSE_STATUS_MONITOR_ENABLE_REQ enableMonitorReq = new GW_HOUSE_STATUS_MONITOR_ENABLE_REQ();
        this.sendRequest(enableMonitorReq);

        this.bridgeHandler.updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE, "");

        startKeepAliveJob();

        while (!Thread.interrupted() && this.klfSocket != null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }

            if (this.receiverJob != null && this.receiverJob.isDone()) {
                logger.warn("Receiver job ended!");
                break;
            }
        }
    }

    private void dispose() {
        this.bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                "Communication lost");

        stopKeepAliveJob();
        stopReceiverJob();

        IOUtils.closeQuietly(this.outputStream);
        IOUtils.closeQuietly(this.klfSocket);

        logger.debug("Connection Manager exiting...");
    }

    /**
     * Start a unique job in charge of receiving messages from KLF 200 unit. If this job ends, the connection should be
     * marked as dead.
     */
    private void startReceiverJob() {
        ScheduledFuture<?> localJob = this.receiverJob;
        if (localJob == null || localJob.isCancelled()) {
            this.receiverJob = this.scheduler.schedule(new VeluxKlf200ResponseReceiver(this), 0, TimeUnit.SECONDS);
        }
    }

    private void stopReceiverJob() {
        ScheduledFuture<?> localJob = this.receiverJob;
        if (localJob != null && !localJob.isCancelled()) {
            localJob.cancel(true);
        }
        this.receiverJob = null;
    }

    private Socket connect(String hostname, int port)
            throws KeyManagementException, NoSuchAlgorithmException, IOException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null, trustAllCerts, null);
        SSLSocket klfSocket = (SSLSocket) ctx.getSocketFactory().createSocket();
        klfSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
        klfSocket.connect(new InetSocketAddress(hostname, port), SOCKET_CONNECT_TIMEOUT);
        klfSocket.startHandshake();

        return klfSocket;
    }

    public @Nullable Socket getKlfSocket() {
        return this.klfSocket;
    }

    public void handleResponse(BaseResponse response) {
        logger.trace("Handling response: {}", response);
        if (response instanceof BaseConfirmationResponse) {
            // response is a confirmation, try to acknowledge a running request
            BaseConfirmationResponse responseCfm = (BaseConfirmationResponse) response;
            BaseRequest<?> localRunningCommand = this.runningCommand;
            if (localRunningCommand == null) {
                logger.error("Received a command CFM, but no command is running!");
            } else {
                localRunningCommand.handleResponse(responseCfm);

                synchronized (localRunningCommand) {
                    localRunningCommand.notifyAll();
                }
            }
        }

        if (response instanceof BaseEvent) {
            // response is an event, notify listeners
            EventBroker.notifyEvent((BaseEvent) response);
        }
    }

    /**
     * Custom trust manager that accepts all certs.
     */
    private final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate @Nullable [] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate @Nullable [] arg0, @Nullable String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate @Nullable [] arg0, @Nullable String arg1)
                throws CertificateException {
        }
    } };

    /**
     * Keep alive ensures that the web socket connection is used and does not time out.
     */
    private void startKeepAliveJob() {
        ScheduledFuture<?> localJob = this.keepAliveJob;
        if (localJob == null || localJob.isCancelled()) {
            logger.debug("Scheduling keepalive job");
            this.keepAliveJob = this.scheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    logger.debug("Sending keepalive");
                    GW_GET_STATE_REQ getStateReq = new GW_GET_STATE_REQ();
                    sendRequest(getStateReq);
                }
            }, this.keepaliveInterval, this.keepaliveInterval, TimeUnit.MINUTES);
        }
    }

    private void stopKeepAliveJob() {
        ScheduledFuture<?> localJob = this.keepAliveJob;
        if (localJob != null && !localJob.isCancelled()) {
            localJob.cancel(true);
        }
        this.keepAliveJob = null;
    }

}
