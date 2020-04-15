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
import org.openhab.binding.veluxklf200.internal.components.VeluxRunStatus;
import org.openhab.binding.veluxklf200.internal.components.VeluxStatusReply;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request all nodes in a given scene to stop at their current position.
 *
 * @author emmanuel
 */
public class KlfCmdStopScene extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdStopScene.class);
    private byte sceneId;

    /**
     * Default constructor.
     *
     * @param sceneId The scene ID to stop
     */
    public KlfCmdStopScene(byte sceneId) {
        super();
        this.sceneId = sceneId;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_STOP_SCENE_CFM:
                switch (data[FIRSTBYTE]) {
                    case 0:
                        logger.debug("Request to stop scene accepted");
                        break;
                    case 1:
                        logger.error("Request to stop scene rejected - Invalid Parameter");
                        this.commandStatus = CommandStatus.ERROR
                                .setErrorDetail("Request to execute scene rejected - Invalid Parameter");
                        break;
                    case 2:
                        logger.error("Request to stop scene rejected - Request Rejected");
                        this.commandStatus = CommandStatus.ERROR
                                .setErrorDetail("Request to execute scene rejected - Request Rejected");
                        break;
                }
                return true;
            case GW_SESSION_FINISHED_NTF:
                logger.debug("Finished stopping the scene");
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            case GW_COMMAND_RUN_STATUS_NTF:
                VeluxRunStatus runStatus = VeluxRunStatus.createFromCode(data[FIRSTBYTE + 7]);
                VeluxStatusReply statusReply = VeluxStatusReply.create(data[FIRSTBYTE + 8]);
                logger.trace(
                        "Notification for Node {}, relating to function parameter {}, Run status is: {}, Command status is: {} ",
                        data[FIRSTBYTE + 3], data[FIRSTBYTE + 4], runStatus, statusReply);
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.STOP_SCENE;
    }

    @Override
    protected byte[] pack() {
        byte[] data = new byte[5];
        data[0] = (byte) (this.getSessionID() >>> 8);
        data[1] = (byte) this.getSessionID();
        data[2] = CMD_ORIGINATOR_USER;
        data[3] = CMD_PRIORITY_NORMAL;
        data[4] = this.sceneId;
        return data;
    }

    @Override
    protected int extractSession(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SESSION_FINISHED_NTF:
                // case KLFCommandCodes.GW_COMMAND_REMAINING_TIME_NTF:
            case GW_COMMAND_RUN_STATUS_NTF:
                return KLFUtils.extractTwoBytes(data, FIRSTBYTE);
            // case KLFCommandCodes.GW_NODE_STATE_POSITION_CHANGED_NTF:
            // This command does not include a session parameter, so just return
            // our own current session ID instead
            // return this.getSessionID();
            case GW_STOP_SCENE_CFM:
            default:
                return KLFUtils.extractTwoBytes(data, FIRSTBYTE + 1);
        }
    }

}