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
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get all groups.
 *
 * @author emmanuel
 */
public class KlfCmdGetAllGroups extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdGetAllGroups.class);
    // private List<VeluxNode> nodes;
    private static byte ACCEPTED = 0;
    private final byte NO_FILTER = 0;
    private final byte USE_FILTER = 1;
    private final byte FILTER_USER_GROUP = 0; // The group type is a user group.
    private final byte FILTER_ROOM = 1; // The group type is a Room.
    private final byte FILTER_HOUSE = 2; // The group type is a House

    /**
     * Default constructor.
     */
    public KlfCmdGetAllGroups() {
        super();
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_ALL_GROUPS_INFORMATION_CFM:
                if (data[FIRSTBYTE] == ACCEPTED) {
                    byte totalNumberOfGroups = data[FIRSTBYTE + 1];
                    logger.trace("Command executing, expecting data for {} groups.", totalNumberOfGroups);
                } else {
                    logger.error("Command has been rejected by the KLF200 unit.");
                    this.setStatus(CommandStatus.ERROR);
                }
                return true;
            case GW_GET_ALL_GROUPS_INFORMATION_NTF:
                byte groupId = KLFUtils.extractOneByte(data, FIRSTBYTE); // groupId
                short order = KLFUtils.extractTwoBytes(data, FIRSTBYTE + 1); // Order
                byte placement = KLFUtils.extractOneByte(data, FIRSTBYTE + 3); // Placement
                String name = KLFUtils.extractUTF8String(data, FIRSTBYTE + 4, FIRSTBYTE + 67); // Name
                byte velocity = KLFUtils.extractOneByte(data, FIRSTBYTE + 68); // Velocity
                byte nodeVariation = KLFUtils.extractOneByte(data, FIRSTBYTE + 69); // NodeVariation
                byte groupType = KLFUtils.extractOneByte(data, FIRSTBYTE + 70); // GroupType
                byte nbrOfObjects = KLFUtils.extractOneByte(data, FIRSTBYTE + 71); // NbrOfObjects
                // byte actuatorBitArray = KLFUtils.extractOneByte(data, FIRSTBYTE + 69); // ActuatorBitArray
                // byte revision = KLFUtils.extractOneByte(data, FIRSTBYTE + 69); // Revision

                logger.debug(
                        "Found group id: {}, order: {}, placement: {}, name: {}, velocity: {}, nodeVariation: {}, groupType: {}, nbrOfObjects: {}",
                        groupId, order, placement, name, velocity, nodeVariation, groupType, nbrOfObjects);
                return true;
            case GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF:
                logger.trace("Command completed, data for all groups recieved");
                this.setStatus(CommandStatus.COMPLETE);
                return true;
            default:
                return false;
        }

    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_ALL_GROUPS_INFORMATION;
    }

    @Override
    protected byte[] pack() {
        byte[] data = new byte[2];

        data[0] = NO_FILTER; // UseFilter
        data[1] = FILTER_HOUSE; // GroupType (if filtering)
        return data;
    }

    @Override
    public boolean isSessionRequired() {
        return false;
    }

    @Override
    public boolean isNodeSpecific() {
        return false;
    }

    @Override
    public KLFGatewayCommands getCommand() {
        return KLFGatewayCommands.GW_GET_ALL_GROUPS_INFORMATION_REQ;
    }
}