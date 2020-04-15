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
import org.openhab.binding.veluxklf200.internal.components.VeluxNode;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get information on a node.
 *
 * @author emmanuel
 */
public class KlfCmdGetNodeInformation extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdGetNodeInformation.class);
    private byte nodeId;
    private VeluxNode node;

    /**
     * Default constructor.
     *
     * @param nodeId The node ID
     */
    public KlfCmdGetNodeInformation(byte nodeId) {
        super();
        this.nodeId = nodeId;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public byte getNodeId() {
        return this.nodeId;
    }

    /**
     * Gets the node.
     *
     * @return the node
     */
    public VeluxNode getNode() {
        return this.node;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_NODE_INFORMATION_CFM:
                if (data[FIRSTBYTE] == 0) {
                    // Command has been accepted by the bridge
                    logger.trace("Command executing, expecting data for node Id: {}.", data[FIRSTBYTE + 1]);
                } else {
                    // Command has been rejected by the bridge
                    logger.error("Command has been rejected by the KLF200 unit.");
                    this.commandStatus = CommandStatus.ERROR;
                }
                return true;
            case GW_GET_NODE_INFORMATION_NTF:
                logger.trace("Get Node: {}", KLFUtils.formatBytes(data));
                VeluxNode node = new VeluxNode(KLFUtils.extractOneByte(data, FIRSTBYTE), // NodeID
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 1), // Order
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 3), // Placement
                        KLFUtils.extractUTF8String(data, FIRSTBYTE + 4, FIRSTBYTE + 67), // Name
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 68), // Velocity
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 69), // NodeTypeSubType
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 71), // Product group
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 72), // Product type
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 73), // Node variation
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 74), // Power Mode
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 75), // buildNumber
                        String.valueOf(KLFUtils.extractFourBytes(data, FIRSTBYTE + 76)), // serial number
                        KLFUtils.extractOneByte(data, FIRSTBYTE + 84), // State
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 85), // currentPosition
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 87), // targetPosition
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 89), // FP1
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 91), // FP2
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 93), // FP3
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 95), // FP4
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 97), // remainingTime
                        KLFUtils.extractUnsignedInt32(data, FIRSTBYTE + 99) // lastCommand
                );
                logger.trace("Retrieved information successfully for node '" + node.getName() + "'.");
                this.node = node;
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_NODE_INFORMATION;
    }

    @Override
    protected byte[] pack() {
        setMainNode(this.nodeId);
        return new byte[] { this.nodeId };
    }

    @Override
    protected byte extractNode(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_NODE_INFORMATION_CFM:
                return data[FIRSTBYTE + 1];
            case GW_GET_NODE_INFORMATION_NTF:
                return data[FIRSTBYTE];
            default:
                return BaseKLFCommand.NOT_REQUIRED;
        }

    }
    /*
     * @Override
     * public boolean equals(Object obj) {
     * if (obj == this) {
     * return true;
     * }
     * if (!(obj instanceof KlfCmdGetNodeInformation)) {
     * return false;
     * }
     *
     * KlfCmdGetNodeInformation cmd = (KlfCmdGetNodeInformation) obj;
     * return Objects.equals(this.nodeId, cmd.nodeId);
     * }
     *
     * @Override
     * public int hashCode() {
     * return Objects.hash(getKLFCommandStructure().getCommandCode(), this.nodeId);
     * }
     */
}