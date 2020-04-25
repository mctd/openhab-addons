/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.status.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set node velocity.
 *
 * @author emmanuel
 */
public class KlfCmdSetVelocity extends BaseKLFCommand {

    private final int CMD_STATUS_ACCEPTED = 0;
    private final int CMD_ERROR_REQUEST_REJECTED = 1;
    private final int CMD_ERROR_INVALID_SYSTEM_TABLE_INDEX = 2;

    private final Logger logger = LoggerFactory.getLogger(KlfCmdSetVelocity.class);
    private byte nodeId;
    private Velocity velocity;

    /**
     * Default constructor.
     *
     * @param nodeId
     *            the node id
     * @param velocity
     *            The desired velocity for specified node.
     */
    public KlfCmdSetVelocity(byte nodeId, Velocity velocity) {
        super();
        this.nodeId = nodeId;
        this.velocity = velocity;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SET_NODE_VELOCITY_CFM:
                byte status = data[FIRSTBYTE];
                byte nodeId = data[FIRSTBYTE + 1];
                switch (status) {
                    case CMD_STATUS_ACCEPTED:
                        logger.debug("Command accepted for node: {}", nodeId);
                        this.setStatus(CommandStatus.COMPLETE);
                        break;
                    case CMD_ERROR_REQUEST_REJECTED:
                        logger.warn("The command was rejected for node {}.", nodeId);
                        this.setStatus(CommandStatus.ERROR);
                        break;
                    case CMD_ERROR_INVALID_SYSTEM_TABLE_INDEX:
                        logger.warn("The command failed: invalid system table index {}.", nodeId);
                        this.setStatus(CommandStatus.ERROR);
                        break;
                    default:
                        logger.error("An unknown confirmation code was recieved: {}, marking the command as ERROR.",
                                status);
                        this.setStatus(CommandStatus.ERROR);
                        break;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.SET_NODE_VELOCITY;
    }

    @Override
    protected byte[] pack() {
        setMainNode(this.nodeId);

        byte[] data = new byte[2];
        data[0] = this.nodeId;
        data[1] = this.velocity.getCode();

        return data;
    }

    @Override
    protected byte extractNode(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SET_NODE_VELOCITY_CFM:
                return data[FIRSTBYTE + 1];
            default:
                logger.error("Unknown response command.");
                return BaseKLFCommand.NOT_REQUIRED;
        }
    }

    @Override
    public boolean isSessionRequired() {
        return false;
    }

    @Override
    public boolean isNodeSpecific() {
        return true;
    }

    @Override
    public KLFGatewayCommands getCommand() {
        return KLFGatewayCommands.GW_SET_NODE_VELOCITY_REQ;
    }
}