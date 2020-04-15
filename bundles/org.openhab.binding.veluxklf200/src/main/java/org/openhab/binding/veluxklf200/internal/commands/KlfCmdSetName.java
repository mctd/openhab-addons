/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import java.nio.charset.StandardCharsets;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set node name.
 *
 * @author emmanuel
 */
public class KlfCmdSetName extends BaseKLFCommand {

    private final int CMD_STATUS_ACCEPTED = 0;
    private final int CMD_ERROR_REQUEST_REJECTED = 1;
    private final int CMD_ERROR_INVALID_SYSTEM_TABLE_INDEX = 2;

    private final Logger logger = LoggerFactory.getLogger(KlfCmdSetName.class);
    private byte nodeId;
    private String name;

    /**
     * Default constructor.
     *
     * @param nodeId
     *            the node id
     * @param name
     *            The new name for specified node.
     */
    public KlfCmdSetName(byte nodeId, String name) {
        super();
        this.nodeId = nodeId;
        this.name = name;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SET_NODE_NAME_CFM:
                byte status = data[FIRSTBYTE];
                byte nodeId = data[FIRSTBYTE + 1];
                switch (status) {
                    case CMD_STATUS_ACCEPTED:
                        logger.debug("Command accepted for node: {}", nodeId);
                        this.commandStatus = CommandStatus.COMPLETE;
                        break;
                    case CMD_ERROR_REQUEST_REJECTED:
                        logger.warn("The command was rejected for node {}.", nodeId);
                        this.commandStatus = CommandStatus.ERROR;
                        break;
                    case CMD_ERROR_INVALID_SYSTEM_TABLE_INDEX:
                        logger.warn("The command failed: invalid system table index {}.", nodeId);
                        this.commandStatus = CommandStatus.ERROR;
                        break;
                    default:
                        logger.error("An unknown confirmation code was recieved: {}, marking the command as ERROR.",
                                status);
                        this.commandStatus = CommandStatus.ERROR;
                        break;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.SET_NODE_NAME;
    }

    @Override
    protected byte[] pack() {
        setMainNode(this.nodeId);

        byte[] nameArray = this.name.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[1 + 64]; // name is 64 bytes long
        data[0] = this.nodeId;
        System.arraycopy(nameArray, 0, data, 1, nameArray.length);

        return data;
    }

    @Override
    protected byte extractNode(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SET_NODE_NAME_CFM:
                return data[FIRSTBYTE + 1];
            default:
                logger.error("Unknown response command.");
                return BaseKLFCommand.NOT_REQUIRED;
        }
    }
}