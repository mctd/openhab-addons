/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.engine;

import java.io.IOException;
import java.util.Iterator;

import org.openhab.binding.veluxklf200.internal.commands.BaseKLFCommand;
import org.openhab.binding.veluxklf200.internal.commands.CommandStatus;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdTerminate;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.response.ResponseFactory;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.components.VeluxErrorResponse;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the response consumer thread class. It reads and processes responses from the KLF200 unit.
 *
 * @author emmanuel - Initial contribution
 *
 */
class KLFResponseConsumer implements Runnable {
    /** Logging. */
    private final Logger logger = LoggerFactory.getLogger(KLFResponseConsumer.class);

    /** Flag indicating if thread should shutdown */
    private boolean queueShutdown = false;

    /** Maximum size of a response from the KLF200 unit. */
    private static final int CONNECTION_BUFFER_SIZE = 4096;

    /** Reference to the parent command processor */
    private final KLFCommandProcessor processor;

    public KLFResponseConsumer(KLFCommandProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        logger.debug("Starting the response consumer. Thread Id: {}", Thread.currentThread().getId());
        while (!queueShutdown) {
            try {
                byte[] receiveBuffer = new byte[CONNECTION_BUFFER_SIZE];
                int messageLength = processor.klfInputStream.read(receiveBuffer, 0, receiveBuffer.length);
                if (messageLength == -1) {
                    // Most likely the stream has closed, so shutdown the queue
                    logger.warn("Received end of file while reading on KLF200 socket, shuting down.");
                    queueShutdown = true;
                } else {
                    // Copy the byte array to a properly sized container
                    byte[] slipFrame = new byte[messageLength];
                    System.arraycopy(receiveBuffer, 0, slipFrame, 0, messageLength);
                    logger.trace("Received response (raw): {}", KLFUtils.formatBytes(slipFrame));

                    KLFCommandFrame commandFrame = KLFCommandFrame.fromSlipFrame(slipFrame);
                    BaseResponse response = ResponseFactory.createFromCommandFrame(this.processor, commandFrame);
                    // TODO: Here we have the response object (or null if not handled). Handle it!

                    if (response != null) {
                        // if (response.isNotification()) {
                        // TODO
                        // processor.eventNotification.notifyEvent(response);
                        // }
                    } else {
                        logger.warn("Unable to understand response.");
                    }

                    byte decoded[] = KLFUtils.slipRFC1055decode(slipFrame);
                    KLFGatewayCommands responseCommand = KLFGatewayCommands
                            .fromNumber(KLFUtils.decodeKLFCommand(decoded));
                    // TODO: handle responseCommand==null (in case the response command is not yet implemented)

                    logger.trace("Received response (decoded): {}: {}", responseCommand, KLFUtils.formatBytes(decoded));

                    if ((decoded != null) && (BaseKLFCommand.validateKLFResponse(decoded))) {
                        if (KLFGatewayCommands.GW_ERROR_NTF == responseCommand) {
                            // General error notification received
                            VeluxErrorResponse error = VeluxErrorResponse
                                    .createFromCode(slipFrame[BaseKLFCommand.FIRSTBYTE]);
                            logger.error("Error Notification Recieved: {}", error);
                            handleGeneralError(error);
                        } else {
                            // Notify if the received command is watched
                            if (processor.eventNotification.isOnWatchList(responseCommand)) {
                                logger.trace("received command {} is on watch list, notifying.", responseCommand);
                                processor.eventNotification.notifyEvent(responseCommand, decoded);
                            }

                            BaseKLFCommand command = findInProgressCommand(responseCommand, decoded);
                            if (command != null) {
                                synchronized (command) {
                                    command.handleResponse(responseCommand, decoded);
                                    switch (command.getStatus()) {
                                        case ERROR:
                                            logger.trace("Response is in an error state for command {}.",
                                                    command.getCommand().name());
                                        case COMPLETE:
                                            logger.trace(
                                                    "Response processed for command {}. No further responses expected, notifying observers.",
                                                    command.getCommand().name());
                                            processor.getCommandsInProgress().remove(command);
                                            command.notifyAll();
                                            break;
                                        case PROCESSING:
                                            // Do nothing, command expecting further responses
                                            logger.trace(
                                                    "Response recieved for command {}, but expecting further responses.",
                                                    command.getCommand().name());
                                            break;
                                        default:
                                            // Should never happen as no other states are valid at this stage.
                                            logger.error(
                                                    "An unexpected condition occurred. A {} command was found with status '{}', this is not permitted at this time.",
                                                    command.getCommand().name(), command.getStatus());
                                            break;
                                    }
                                }
                            } else {
                                // Only report an error in the event that the command received is not on our watch list.
                                // Commands that are on the watch list are notification commands that may have
                                // originated as a result of a direct user interaction with a velux device (such as
                                // using a remote control to perform a task)
                                if (!processor.eventNotification.isOnWatchList(responseCommand)) {
                                    logger.warn(
                                            "Recieved response but unable to find a matching command to consume the response. Discarding response: {}",
                                            KLFUtils.formatBytes(decoded));
                                }
                            }
                        }
                    } else {
                        logger.error("Unable to SLIP RFC1055 decode the payload recieved, discarding. {}",
                                KLFUtils.formatBytes(slipFrame));
                    }
                }
            } catch (IOException e) {
                if (processor.isCommunicationStopped()) {
                    logger.info(
                            "Error caught when awaiting a response from KLF200 unit, but as the command processor is marked to shutdown, assuming it's ok: {}",
                            e.getMessage());
                } else {
                    logger.error("Unexpected error when awaiting a response from KLF200 unit: {}", e.getMessage());
                }
                // Mark queue shutdown as this is an unrecoverable error. Watchdog will restart the connection
                queueShutdown = true;
            }
        }
        logger.warn("The response consumer has been shutdown.");

        // Ask the command consumer to terminate
        processor.getCommandQueue().offerFirst(new KlfCmdTerminate());

        if (processor.klfRawSocket != null) {
            try {
                // Closing socket so the watchdog can detect something went wrong
                processor.klfRawSocket.close();
            } catch (IOException e) {
                logger.error("Error while closing communication socket.");
            }
        }
    }

    /**
     * In the event that an error frame is received, it may or may not be
     * possible to determine which original command the error refers to. If we
     * only have a single command in the processing state, then it is likely
     * that the error pertains to this command.
     *
     * @param error
     *            The details of the error that occurred.
     */
    private void handleGeneralError(VeluxErrorResponse error) {
        if (processor.getCommandsInProgress().size() == 1) {
            // Likely that the offending Command is the only one in the list.
            BaseKLFCommand bad = processor.getCommandsInProgress().iterator().next();
            processor.getCommandsInProgress().remove(bad);
            bad.setStatus(CommandStatus.ERROR.setErrorDetail(error.getErrorReason()));
            synchronized (bad) {
                bad.notifyAll();
            }
        }
    }

    /**
     * When a response is received from the KLF200 unit, the response code
     * (command code) of the response is retrieved and compared with the
     * {@link processingList} list of commands that are in progress. One of the
     * commands in the list should be able to handle the specified response
     * message from the KLF200.
     *
     * @param commandCode The response code (command code) received from the KLF200
     *            unit.
     * @param data the data
     * @return The relevant command from the {@link processingList} list or null
     *         if no match is found.
     */
    private BaseKLFCommand findInProgressCommand(KLFGatewayCommands responseCommand, byte[] data) {
        BaseKLFCommand result = null;
        Iterator<BaseKLFCommand> iterator = processor.getCommandsInProgress().iterator();
        while (iterator.hasNext()) {
            BaseKLFCommand commandInProgress = iterator.next();
            if (commandInProgress.canHandleResponse(responseCommand, data)) {
                logger.trace("Found a matching command for the response {}", responseCommand);
                result = commandInProgress;
            }
        }

        if (result == null) {
            if (!processor.eventNotification.isOnWatchList(responseCommand)) {
                // Only report an error in the event that the command received is not on our watch list.
                // Commands that are on the watch list are notification commands that may have originated as a result of
                // a direct user interaction with a velux device (such as using a remote control to perform a task)
                logger.error("No match found for the command with the response {}", responseCommand);
            } else {
                logger.debug("No command can handle the response {}, but it is watched as a notification.",
                        responseCommand);
            }
        }

        return result;
    }
}
