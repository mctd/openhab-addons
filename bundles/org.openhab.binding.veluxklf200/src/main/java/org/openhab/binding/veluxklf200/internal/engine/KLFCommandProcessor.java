/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.openhab.binding.veluxklf200.internal.commands.BaseKLFCommand;
import org.openhab.binding.veluxklf200.internal.commands.CommandStatus;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdLogin;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdPing;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdTerminate;
import org.openhab.binding.veluxklf200.internal.handler.KLF200BridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the work-horse of the KLF200 interface. Specifically, it is
 * responsible for establishing connections to the unit, sending commands and
 * then delegating responses. Two queue's are maintained, one with a list of
 * commands that are awaiting processing and a second with a list of commands
 * that are being processed. A producer consumer pattern is implemented whereby
 * clients can submit a command and then notified when the processing has
 * completed.
 *
 * @author emmanuel
 */
public class KLFCommandProcessor {

    private final Logger logger = LoggerFactory.getLogger(KLFCommandProcessor.class);
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final long DEFAULT_COMMAND_TIMEOUT = 60000;
    private static final int KEEPALIVE_PING_FREQ = 1 * 60 * 10;
    private static final int MAX_QUEUE_SIZE = 20;
    private static final int WATCHDOG_DELAY = 2000;
    private String host;
    private int port;
    private String password;
    private Timer keepaliveTimer;
    private boolean communicationStopped = false;
    private boolean loggedIn;
    private LinkedBlockingDeque<BaseKLFCommand> commandQueue;

    /**
     * List of commands that are in the process of being processed by the KLF200
     * unit.
     */
    private CopyOnWriteArrayList<BaseKLFCommand> processingList;

    /**
     * Reference to class to handle unsolicited events. Specifically, events
     * that have occurred within the KLF/Velux eco-system that did not originate
     * as a result of a command initiated by us! For example, if someone uses a
     * remote control to open a blind.
     */
    KLFEventNotification eventNotification;

    /** Socket connection to the KLF200 unit. */
    SSLSocket klfRawSocket = null;

    /** Output data stream related to the {@link klfRawSocket}. */
    DataOutputStream klfOutputStream;

    /** Input data stream related to the {@link klfRawSocket}. */
    DataInputStream klfInputStream;

    /** Response consumer Thread reference */
    private Thread responseConsumerThread;

    /** Command consumer Thread reference */
    private Thread commandConsumerThread;

    /** Watchdog Thread reference */
    private Thread watchdogThread;

    /** Response consumer Thread reference */
    private KLF200BridgeHandler bridgeHandler;

    /**
     * Custom trust manager that accepts self-signed certs. The KLF200 unit uses
     * SSL for communication, but does have a valid commercial SSL cert. It uses
     * a self-signed cert.
     */
    private final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }
    } };

    /**
     * Instantiates a new KLF command processor.
     *
     * @param host
     *            The IP address or hostname of the KLF200 unit.
     * @param port
     *            The TCP port of the KLF200 unit.
     * @param password
     *            The password of the KLF200 unit.
     */
    public KLFCommandProcessor(KLF200BridgeHandler bridgeHandler, String host, int port, String password) {
        this.bridgeHandler = bridgeHandler;
        this.host = host;
        this.port = port;
        this.password = password;
        this.processingList = new CopyOnWriteArrayList<BaseKLFCommand>();
        this.commandQueue = new LinkedBlockingDeque<BaseKLFCommand>(MAX_QUEUE_SIZE);
        this.eventNotification = new KLFEventNotification();
    }

    /**
     * Indicates communication status.
     *
     * @return True if communication is or should be stopped. True otherwise.
     */
    public boolean isCommunicationStopped() {
        return this.communicationStopped;
    }

    /**
     * Indicates if connection to KLF unit is authenticated.
     *
     * @return True if connection is authenticated, false otherwise.
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    CopyOnWriteArrayList<BaseKLFCommand> getCommandsInProgress() {
        return this.processingList;
    }

    LinkedBlockingDeque<BaseKLFCommand> getCommandQueue() {
        return this.commandQueue;
    }

    /**
     * After constructing, initialize should be called to prepare for
     * processing. A connection to the KLF200 unit is established, login is
     * attempted and the processing queues are setup to wait for commands.
     *
     * @return true, If initialization is successful, false otherwise. If
     *         initialization fails and processing operations are attempted,
     *         runtime exceptions will be thrown.
     */
    public void initialize() {
        // Start the watchdog thread
        watchdogThread = new Thread("KLFWatchdog") {
            @Override
            public void run() {
                logger.debug("Starting the watchdog thread (Id: {}).", Thread.currentThread().getId());

                startCommunication();

                while (!communicationStopped) {
                    try {
                        sleep(WATCHDOG_DELAY);
                    } catch (InterruptedException e) {
                        logger.warn("Watchdog sleeping has been interrupted.");
                    }

                    logger.trace("Checking if communication is up.");
                    if (klfRawSocket == null || !klfRawSocket.isConnected() || klfRawSocket.isClosed()) {
                        // Assuming connection is down if socket is either null, not connected or closed.
                        logger.warn("Communication to KLF200 down, restarting it");
                        stopCommunication();
                        startCommunication();
                    }
                }

                stopCommunication();
                logger.debug("Watchdog thread terminating.");
            }
        };
        watchdogThread.start();
    }

    private boolean isCommunicationUp() {

        if (klfRawSocket != null) {
            logger.debug("klfRawSocket: isConnected(): {}, isClosed(): {}", klfRawSocket.isConnected(),
                    klfRawSocket.isClosed());
        } else {
            logger.trace("klfRawSocket is null");
        }
        return klfRawSocket != null && klfRawSocket.isConnected() && !klfRawSocket.isClosed();
    }

    /**
     * Starts the communication to KLF200
     *
     * @throws Exception
     */
    private synchronized void startCommunication() {
        logger.debug("Starting communication.");
        if (isCommunicationUp()) {
            logger.error("Communication already up. Please stop it first.");
            return;
        }

        logger.debug("Attempting to create an SSL connection to KLF200 host {} on port {}.", host, port);
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("SSL");

            ctx.init(null, trustAllCerts, null);

            klfRawSocket = (SSLSocket) ctx.getSocketFactory().createSocket();

            klfRawSocket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
            klfRawSocket.startHandshake();

            klfOutputStream = new DataOutputStream(klfRawSocket.getOutputStream());
            klfInputStream = new DataInputStream(klfRawSocket.getInputStream());
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            if (klfRawSocket != null) {
                try {
                    klfRawSocket.close();
                } catch (Exception e1) {
                    logger.debug("Error closing klfOutputStream: {}", e1.getMessage());
                }
                klfRawSocket = null;
            }

            klfOutputStream = null;
            klfInputStream = null;
            String err = String.format("Unable to connect to KLF200 host %s on port %d. Reason: %s", host, port,
                    e.getMessage());
            logger.error(err);
            bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, err);
            return;
        }

        logger.info("Successfully established an SSL connection to KLF200 host {} on port {}.", host, port);

        // bridgeHandler.updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE, null);

        // Start the consumer threads
        responseConsumerThread = new Thread(new KLFResponseConsumer(this), "KLFResponseConsumer");
        responseConsumerThread.start();
        logger.trace("ResponseConsumer Thread id: {}", responseConsumerThread.getId());

        commandConsumerThread = new Thread(new KLFCommandConsumer(this), "KLFCommandConsumer");
        commandConsumerThread.start();
        logger.trace("CommandConsumer Thread id: {}", commandConsumerThread.getId());

        // Login to KLF 200
        logger.debug("Attempting to login to the KLF200 unit with password supplied.");
        KlfCmdLogin loginCommand = new KlfCmdLogin(password);
        if (executeCommand(loginCommand) && loginCommand.getStatus() == CommandStatus.COMPLETE) {
            logger.info("Successfully logged in to the KLF200 unit @ {}:{}", host, port);
            this.loggedIn = true;
        } else {
            String errMsg = "Unable to login to the KLF200 unit with password supplied.";
            logger.error(errMsg);
            bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, errMsg);
            return;
            // TODO : this lets the socket open, the threads running, but not logged in !
        }

        // Refresh Bridge Properties and Node status
        bridgeHandler.updateBridgeProperties();
        // bridgeHandler.refreshKnownDevices();

        /**
         * Schedule a ping of the KLF200 every 10 minutes to prevent the socket from
         * shutting down.
         */
        keepaliveTimer = new Timer();
        keepaliveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.debug("Sending keep-alive ping to the KLF 200 unit.");
                executeCommandAsync(new KlfCmdPing());
            }
        }, 1000 * KEEPALIVE_PING_FREQ, 1000 * KEEPALIVE_PING_FREQ);

        bridgeHandler.updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE, null);
    }

    /**
     * Stops the communication to KLF200
     */
    private synchronized void stopCommunication() {
        logger.debug("Stopping KLF200 communication.");
        bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Shutting down");

        // Don't ask the command consumer to suicide if it didn't even started once
        if (commandConsumerThread != null) {
            executeCommandAsync(new KlfCmdTerminate());
        }

        // Stopping the keepalive timer
        if (keepaliveTimer != null) {
            keepaliveTimer.cancel();
            keepaliveTimer = null;
        }

        // Closing the communication socket
        if (klfRawSocket != null) {
            logger.debug("Closing socket.");
            try {
                klfRawSocket.close();
            } catch (IOException e) {
                logger.error("Error trying to close socket.");
                // discard IOException as we're trying to close the socket anyway
            }
        }
        klfRawSocket = null;

        this.loggedIn = false;

        // join the consumer threads (wait for them to end)
        if (commandConsumerThread != null) {
            logger.debug("Waiting for commandConsumerThread to end.");
            try {
                commandConsumerThread.join();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting consumer threads to end.");
            }
        }

        if (responseConsumerThread != null) {
            logger.debug("Waiting for responseConsumerThread to end.");
            try {
                responseConsumerThread.join();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting consumer threads to end.");
            }
        }

        // Empty the command queue and the processing list
        this.commandQueue.clear();
        this.processingList.clear();

        logger.debug("communication with KLF200 stopped");
        bridgeHandler.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Stopped");
    }

    /**
     * Indicates if the command processor is active and processing commands and responses.
     *
     * @return true if command processor is running, false otherwise.
     */
    public boolean isUpAndRunning() {
        boolean ret = !communicationStopped && commandConsumerThread != null && commandConsumerThread.isAlive()
                && responseConsumerThread != null && responseConsumerThread.isAlive();
        if (!ret) {
            logger.trace("communicationStopped: {}", communicationStopped);
            logger.trace("commandConsumerThread: {}", commandConsumerThread);
            logger.trace("commandConsumerThread.isAlive(): {}", commandConsumerThread.isAlive());
            logger.trace("responseConsumerThread: {}", responseConsumerThread);
            logger.trace("responseConsumerThread.isAlive(): {}", responseConsumerThread.isAlive());
        }
        return ret;
    }

    /**
     * Registers a third-party as a consumer of notification events that were generated by the KLF200 unit.
     *
     * @param listener A third-party event consumer that implements the {@link KLFEventListener} interface.
     */
    public void registerEventListener(KLFEventListener listener) {
        this.eventNotification.registerListener(listener);
    }

    /**
     * Close the socket connection to the KLF200 unit as well as stopping
     * processing queues and threads.
     */
    public void shutdown() {
        logger.debug("Shutting down the command processor.");

        this.communicationStopped = true;
        try {
            watchdogThread.join();
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for watchdog thread to end.");
        }

        logger.info("Shutdown complete.");
    }

    /**
     * Send a command to be processed, but do not block / wait for the command
     * to complete.
     *
     * @param c
     *            The command to be processed
     * @return true, If successfully added to the queue, false if it was not
     *         possible to add to the processing queue. False would typically
     *         indicate that the queue is full.
     */
    public boolean executeCommandAsync(BaseKLFCommand command) {
        // TODO : rather than executing async, we should always wait for a CFM reply (or GW_ERROR_NTF).
        // All reads (CFM or NTF) must be done by a dedicated thread as it cannot be garantied that a NTF message does
        // not come just after sending a REQ request.
        // So if CFM or GW_ERROR_NTF message is receive, and notify the REQ command for completion (if match) ==> put
        // the running command in a shared object that the read thread can access.
        logger.trace("Adding command {} to the command queue.", command.getCommand().name());

        synchronized (command) {
            boolean ret = this.commandQueue.offer(command);

            if (ret) {
                logger.trace("Command {} queued, awaiting processing.", command.getCommand().name());
                command.setStatus(CommandStatus.QUEUED);
            } else {
                command.setStatus(CommandStatus.ERROR);
                logger.error("Command {} could not be added to the queue.", command.getCommand().name());
            }
            return ret;
        }
    }

    /**
     * Similar to {@link dispatchCommand}, however, waits for the command to
     * execute fully before returning to the user.
     *
     * @param c
     *            The command to be processed
     * @return true, If processing is complete, false if processing is
     *         incomplete for any reason. Note: a return value of true only
     *         indicates that processing has finished, it does not signify that
     *         processing was successful. The command object itself needs to be
     *         queried to determine whether or not processing yielded a
     *         successful or expected result.
     */
    public boolean executeCommand(BaseKLFCommand c) {
        return this.executeCommand(c, DEFAULT_COMMAND_TIMEOUT);
    }

    /**
     * Similar to {@link dispatchCommand}, however, waits for the command to
     * execute fully before returning to the user.
     *
     * @param c
     *            The command to be processed
     * @param timeout
     *            How long to wait (in milliseconds) for the command to execute
     * @return true, If processing is complete, false if processing is
     *         incomplete for any reason. Note: a return value of true only
     *         indicates that processing has finished, it does not signify that
     *         processing was successful. The command object itself needs to be
     *         queried to determine whether or not processing yielded a
     *         successful or expected result.
     */
    public boolean executeCommand(BaseKLFCommand command, long timeout) {
        logger.debug("Executing command {}", command.getCommand());
        synchronized (command) {
            if (executeCommandAsync(command)) {
                try {
                    logger.debug("Waiting for command {} to complete.", command.getCommand());
                    command.wait(timeout);
                    // Processing complete (or finished due to error)
                    if (command.getStatus() == CommandStatus.ERROR) {
                        logger.warn("command {} terminated abnormally.", command.getCommand().name());
                        return true;
                    } else if (command.getStatus() == CommandStatus.COMPLETE) {
                        logger.debug("Command {} completed successfully.", command.getCommand().name());
                        return true;
                    } else {
                        // Processing not completed within the allocated time
                        logger.warn("The command did not complete within the {} second time allocated.",
                                timeout / 1000);
                        command.setStatus(CommandStatus.ERROR
                                .setErrorDetail("The command did not complete within the time allocated."));
                        return false;
                    }
                } catch (InterruptedException e) {
                    logger.warn("An unexpected error occurred while waiting for a command to complete: {}",
                            e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }
}