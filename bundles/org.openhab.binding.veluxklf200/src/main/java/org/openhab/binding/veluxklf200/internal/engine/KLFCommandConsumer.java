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
import java.util.concurrent.TimeUnit;

import org.openhab.binding.veluxklf200.internal.commands.BaseKLFCommand;
import org.openhab.binding.veluxklf200.internal.commands.CommandStatus;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdTerminate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the command consumer thread class. It picks commands out from the command queue and send them to the KLF200
 * unit.
 *
 * @author emmanuel - Initial contribution
 *
 */
class KLFCommandConsumer implements Runnable {
    /** Logging. */
    private final Logger logger = LoggerFactory.getLogger(KLFCommandConsumer.class);

    /** Flag indicating if thread should shutdown */
    private boolean queueShutdown = false;

    /** Time delay in milliseconds to wait for a duplicate in progress command to complete */
    private static final long WAIT_DUPLICATE_COMPLETION = 500;

    /** Reference to the parent command processor */
    private final KLFCommandProcessor processor;

    /** Time delay between sending commands to the KLF200 unit */
    private static final long COMMAND_EXEC_DELAY = 250;

    public KLFCommandConsumer(KLFCommandProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        logger.debug("Starting the command consumer. Thread Id: {}", Thread.currentThread().getId());
        logger.debug("QUEUE CONTENTS -> {}", getQueueContents());

        while (!this.queueShutdown) {
            try {
                // Pick a command from the queue (block until a command is available)
                BaseKLFCommand command = this.processor.getCommandQueue().take();
                synchronized (command) {
                    if (isSimilarInProgress(command)) {
                        // The current command is node specific, does not use a session, and there is a similar
                        // command in progress. There is a collision risk, and the KLF200 could get confused. Will put
                        // the command back to queue, waiting for the one in progress to complete
                        logger.debug("Similar node specific command already executing, delaying execution: {}",
                                command);
                        if (!this.processor.getCommandQueue().offerFirst(command)) {
                            logger.error("Error putting command back into queue.");
                        }
                        logger.debug("QUEUE CONTENTS -> {}", getQueueContents());
                        TimeUnit.MILLISECONDS.sleep(WAIT_DUPLICATE_COMPLETION);
                    } else {
                        // Shutdown queue
                        if (command instanceof KlfCmdTerminate) {
                            logger.debug("Kill command received, terminating the consumer thread.");
                            this.queueShutdown = true;
                        } else {
                            processCommand(command);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("Operation of the command consumer interrupted: {}", e.getMessage());
                this.queueShutdown = true;
            }
        }
        logger.debug("The command consumer thread is shuting down.");
        try {
            // Closing socket so the watchdog can detect something went wrong
            this.processor.klfRawSocket.close();
        } catch (IOException e) {
            logger.error("Error while closing communication socket.");
        }
    }

    /**
     * Checks the list of commands that are currently in progress to see if any are similar to the current command
     * supplied. This function is used by the command consumer to determine if it is safe to execute the current
     * command. It would not be safe to execute the current command if that command does not support sessions and there
     * is already a similar command in progress. In this case, the KLF200 can get confused and return only a single
     * result rather than two results -- one for each command.
     *
     * @param current The current command that the command processor is ready to execute
     * @return True if there is a similar command in progress, false otherwise.
     */
    private boolean isSimilarInProgress(BaseKLFCommand current) {
        if (current.isSessionRequired()) {
            return false;
        }

        /*
         * if (!current.isNodeSpecific()) {
         * return false;
         * }
         */

        Iterator<BaseKLFCommand> iterator = this.processor.getCommandsInProgress().iterator();
        while (iterator.hasNext()) {
            BaseKLFCommand inProgress = iterator.next();
            if (inProgress.getClass() == current.getClass()) {
                // command are of same type
                if (!current.isNodeSpecific() || (inProgress.getMainNode() == current.getMainNode())) {
                    // command is not node specific, OR relates to the same node, then a similar command is already in
                    // progress
                    logger.warn("Another similar command ({}) is beeing processed.", current.getClass());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a string representation of all the items currently in various processing states. Used typically for debug or
     * trace logging purposes only.
     *
     * @return String representation of the contents of the queues
     */
    private String getQueueContents() {
        String result = "In Progress: [";
        for (BaseKLFCommand cmd : this.processor.getCommandsInProgress()) {
            String part[] = cmd.getClass().getName().split("\\.");
            result += part[part.length - 1];
            result += ", ";
        }
        result += "], Waiting: [";
        for (BaseKLFCommand cmd : this.processor.getCommandQueue()) {
            String part[] = cmd.getClass().getName().split("\\.");
            result += part[part.length - 1];
            result += ", ";
        }
        result += "]";
        return result;
    }

    /**
     * Process a command. Specifically, validate it and then send to the KLF200 unit. Once sent, the command is added to
     * the {@link processingList} list to await response(s) from the KLF200 unit.
     *
     * @param command
     *            The command to execute.
     */
    private void processCommand(BaseKLFCommand command) {
        if (this.processor.klfOutputStream != null) {
            if (command.isValid()) {
                if (!command.isAuthRequired() || this.processor.isLoggedIn()) {
                    byte[] data = command.getRawKLFCommand();
                    try {
                        logger.debug("Executing command {} with Session: {} for Specific Node: {}",
                                command.getCommand().name(), command.formatSessionID(), command.formatMainNode());

                        this.processor.klfOutputStream.write(data, 0, data.length);
                        this.processor.klfOutputStream.flush();
                        command.setStatus(CommandStatus.PROCESSING);
                        this.processor.getCommandsInProgress().add(command);

                        try {
                            // The KLF200 gets confused when you send commands to it too quickly. When sent too quickly,
                            // the unit can lock up and become unresponsive for long periods of time. As such, a brief
                            // delay between commands being sent appears to cure the problem.
                            TimeUnit.MILLISECONDS.sleep(COMMAND_EXEC_DELAY);
                        } catch (InterruptedException e) {
                            logger.warn("Command execution delay was interrupted: {}", e);
                        }
                        return;
                    } catch (IOException e) {
                        logger.error("Unable to commuinicate with the KLF200 unit: {}", e.getMessage());
                        this.queueShutdown = true;
                    }
                } else {
                    logger.error("Rejected command as command required login, but login has not been performed.");
                }
            } else {
                logger.error("Rejected command as command failed validation.");
            }
        } else {
            logger.error("Rejected command as connection with KLF200 was null.");
        }

        command.setStatus(CommandStatus.ERROR);
        synchronized (command) {
            // If we get here, something has gone wrong, so we need to notify the caller
            command.notifyAll();
        }
    }
}
