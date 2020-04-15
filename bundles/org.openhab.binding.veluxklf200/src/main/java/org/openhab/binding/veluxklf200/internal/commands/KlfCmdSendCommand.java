/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.components.VeluxCommandInstruction;
import org.openhab.binding.veluxklf200.internal.components.VeluxRunStatus;
import org.openhab.binding.veluxklf200.internal.components.VeluxStatusReply;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send activating command direct to one or more io-homecontrol® nodes.
 *
 * @author emmanuel
 */
public class KlfCmdSendCommand extends BaseKLFCommand {

    private final int CMD_STATUS_REJECTED = 0;
    private final int CMD_STATUS_ACCEPTED = 1;

    /** Main function point on unit. */
    public static final byte MAIN_PARAMETER = (byte) 0x0000;

    /** Used to tell the unit to stop actuating. */
    public static final short STOP_PARAMETER = (short) 0xD200;

    private final Logger logger = LoggerFactory.getLogger(KlfCmdSendCommand.class);
    private List<VeluxCommandInstruction> commands;

    /**
     * Constructor variant that creates a command with a single instruction to
     * send.
     *
     * @param nodeId
     *            the node id
     * @param function
     *            The functional parameter of the device that you intend to
     *            control. The 'main parameter' on the device is parameter 0.
     * @param nodeCommand
     *            the node command
     */
    public KlfCmdSendCommand(byte nodeId, byte function, short nodeCommand) {
        super();
        this.commands = new ArrayList<VeluxCommandInstruction>();
        commands.add(new VeluxCommandInstruction(nodeId, function, nodeCommand));
    }

    /**
     * Constructor variant that creates a command with a single instruction to
     * send.
     *
     * @param instruction
     *            the instruction
     */
    public KlfCmdSendCommand(VeluxCommandInstruction instruction) {
        super();
        this.commands = new ArrayList<VeluxCommandInstruction>();
        commands.add(instruction);
    }

    /**
     * Constructor variant that creates a command with a list of instructions to
     * send to the KLF200.
     *
     * @param instructions
     *            the list of instructions
     */
    public KlfCmdSendCommand(List<VeluxCommandInstruction> instructions) {
        super();
        this.commands = new ArrayList<VeluxCommandInstruction>();
        commands.addAll(instructions);
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_COMMAND_SEND_CFM:
                int sessionId = KLFUtils.extractTwoBytes(data, FIRSTBYTE);
                byte status = data[FIRSTBYTE + 2];
                switch (status) {
                    case CMD_STATUS_REJECTED:
                        logger.warn("The command was rejected for session {}, marking the command as ERROR.",
                                sessionId);
                        this.commandStatus = CommandStatus.ERROR;
                        break;
                    case CMD_STATUS_ACCEPTED:
                        logger.debug("Command accepted for session: {}", sessionId);
                        break;
                    default:
                        logger.error("An unknown confirmation code was recieved: {}, marking the command as ERROR.",
                                status);
                        this.commandStatus = CommandStatus.ERROR;
                        break;
                }
                return true;
            case GW_COMMAND_RUN_STATUS_NTF:
                VeluxRunStatus runStatus = VeluxRunStatus.createFromCode(data[FIRSTBYTE + 7]);
                VeluxStatusReply statusReply = VeluxStatusReply.create(data[FIRSTBYTE + 8]);
                logger.debug(
                        "GW_COMMAND_RUN_STATUS_NTF Notification for Node {}, relating to function parameter {}, Session: {}, Run status is: {}, Command status is: {} ",
                        data[FIRSTBYTE + 3], KLFUtils.extractTwoBytes(data, FIRSTBYTE), data[FIRSTBYTE + 4], runStatus,
                        statusReply);
                return true;
            case GW_COMMAND_REMAINING_TIME_NTF:
                logger.debug(
                        "GW_COMMAND_REMAINING_TIME_NTF Notification for Node {}, session: {}, relating to function parameter {}, time remaining to complete is {} seconds",
                        data[FIRSTBYTE + 2], KLFUtils.extractTwoBytes(data, FIRSTBYTE), data[FIRSTBYTE + 3],
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 4));
                return true;
            case GW_SESSION_FINISHED_NTF:
                logger.debug("Processing of the command with session: {} is complete.",
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE));
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.SEND_NODE_COMMAND;
    }

    @Override
    protected byte[] pack() {
        byte[] data = new byte[66];
        // Session ID
        data[0] = (byte) (this.getSessionID() >>> 8);
        data[1] = (byte) this.getSessionID();

        // CommandOriginator
        data[2] = CMD_ORIGINATOR_USER;

        // PriorityLevel
        data[3] = CMD_PRIORITY_NORMAL;

        int counter = 0;
        for (Iterator<VeluxCommandInstruction> it = this.commands.iterator(); it.hasNext();) {
            VeluxCommandInstruction cmd = it.next();
            // ParameterActive
            data[4] = cmd.getFunction();

            // FunctionalParameterValueArray
            data[7 + (counter * 2)] = (byte) (cmd.getPosition() >>> 8);
            data[8 + (counter * 2)] = (byte) cmd.getPosition();

            // IndexArray
            data[42 + counter] = cmd.getNodeId();
            counter++;
        }
        data[41] = (byte) counter;
        return data;
    }

    @Override
    protected int extractSession(KLFGatewayCommands responseCommand, byte[] data) {
        return KLFUtils.extractTwoBytes(data, FIRSTBYTE);
    }

}