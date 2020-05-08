package org.openhab.binding.veluxklf200.internal.handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200Helpers;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.response.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class VeluxKlf200ResponseReceiver implements Runnable {
    private VeluxKlf200Connection klfConnectionManager;
    private @Nullable DataInputStream inputStream;
    private static final int READ_BUFFER_LENGTH = 512;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public VeluxKlf200ResponseReceiver(VeluxKlf200Connection klfConnectionManager) {
        this.klfConnectionManager = klfConnectionManager;
    }

    @Override
    public void run() {
        logger.debug("Starting receiver job.");

        Socket klfSocket = this.klfConnectionManager.getKlfSocket();
        DataInputStream inputStream = null;
        if (klfSocket != null) {
            try {
                inputStream = new DataInputStream(klfSocket.getInputStream());
            } catch (IOException e) {
                logger.error("Unable to create input stream: {}", e.getMessage());
            }
        }

        while (klfSocket != null && inputStream != null && !Thread.interrupted()) {
            byte[] receiveBuffer = new byte[READ_BUFFER_LENGTH];
            int messageLength;
            try {
                messageLength = inputStream.read(receiveBuffer, 0, receiveBuffer.length);
            } catch (SocketTimeoutException te) {
                // Read timeout is normal as socket has setSoTimeout
                continue;
            } catch (IOException e) {
                logger.error("Error reading: {}", e.getMessage());
                break;
            }

            if (messageLength >= 5) {
                // Assuming we only get a single message at once. This may be a mistake. Potential concatenated slip
                // frames should be detected and split.
                byte[] slipFrame = new byte[messageLength];
                System.arraycopy(receiveBuffer, 0, slipFrame, 0, messageLength);
                logger.trace("received: {}", VeluxKlf200Helpers.byteArrayToHexString(slipFrame));

                KLFCommandFrame commandFrame;
                try {
                    commandFrame = KLFCommandFrame.fromSlipFrame(slipFrame);
                } catch (IOException e) {
                    logger.error("Unable to convert received bytes to an understandable response: {}", e.getMessage());
                    continue;
                }

                BaseResponse response = ResponseFactory.createFromCommandFrame(commandFrame,
                        this.klfConnectionManager.getBridgeUID());
                if (response != null) {
                    this.klfConnectionManager.HandleResponse(response);
                } else {
                    logger.error("Unable to build the response for code: {}", commandFrame.getCommand());
                    continue;
                }
            } else if (messageLength >= 0) {
                logger.error("Invalid message length < 1 ({})", messageLength);
                break;
            }

            if (messageLength < 0 || klfSocket.isClosed()) {
                logger.warn("Socket has been closed or reached EOF, exiting.");
                break;
            }
        }

        IOUtils.closeQuietly(this.inputStream);
        logger.debug("Receiver job exiting...");
    }
}
