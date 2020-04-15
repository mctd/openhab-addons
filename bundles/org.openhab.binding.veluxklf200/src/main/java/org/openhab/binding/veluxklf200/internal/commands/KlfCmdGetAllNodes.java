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
import java.util.List;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.components.VeluxNode;
import org.openhab.binding.veluxklf200.internal.components.VeluxNodeType;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get all nodes.
 *
 * @author emmanuel
 */
public class KlfCmdGetAllNodes extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdGetAllNodes.class);
    private List<VeluxNode> nodes;
    private static byte ACCEPTED = 0;

    /**
     * Default constructor.
     */
    public KlfCmdGetAllNodes() {
        super();
        this.nodes = new ArrayList<VeluxNode>();
    }

    /**
     * Gets the list of discovered nodes.
     *
     * @return List of discovered nodes
     */
    public List<VeluxNode> getNodes() {
        return this.nodes;
    }

    /**
     * Gets nodes by type.
     *
     * @param type The type of Nodes to look for
     * @return the Nodes matching type
     */
    public List<VeluxNode> getNodesByType(VeluxNodeType type) {
        ArrayList<VeluxNode> ret = new ArrayList<VeluxNode>();
        for (VeluxNode n : this.nodes) {
            if (n.getNodeTypeSubType() == type) {
                ret.add(n);
            }
        }
        return ret;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_ALL_NODES_INFORMATION_CFM:
                if (data[FIRSTBYTE] == ACCEPTED) {
                    logger.trace("Command executing, expecting data for {} nodes.", data[FIRSTBYTE + 1]);
                } else {
                    logger.error("Command has been rejected by the KLF200 unit.");
                    this.commandStatus = CommandStatus.ERROR;
                }
                return true;
            case GW_GET_ALL_NODES_INFORMATION_NTF:
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
                logger.trace("Found node: " + node.getName());
                this.nodes.add(node);
                return true;
            case GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF:
                logger.trace("Command completed, data for all nodes recieved");
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            default:
                return false;
        }

    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_ALL_NODE_INFORMATION;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}