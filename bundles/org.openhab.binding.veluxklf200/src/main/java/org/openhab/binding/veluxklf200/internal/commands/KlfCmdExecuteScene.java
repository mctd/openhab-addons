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
import org.openhab.binding.veluxklf200.internal.components.VeluxPosition;
import org.openhab.binding.veluxklf200.internal.components.VeluxRunStatus;
import org.openhab.binding.veluxklf200.internal.components.VeluxState;
import org.openhab.binding.veluxklf200.internal.status.StatusReply;
import org.openhab.binding.veluxklf200.internal.status.Velocity;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes a scene.
 *
 * @author emmanuel
 */
public class KlfCmdExecuteScene extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdExecuteScene.class);
    private byte sceneId;
    private Velocity velocity = Velocity.DEFAULT;

    /**
     * Default constructor.
     *
     * @param sceneId The scene ID
     */
    public KlfCmdExecuteScene(byte sceneId) {
        this(sceneId, Velocity.DEFAULT);
    }

    /**
     * Constructor with velocity.
     *
     * @param sceneId The scene ID
     */
    public KlfCmdExecuteScene(byte sceneId, Velocity velocity) {
        super();
        this.sceneId = sceneId;
        this.velocity = velocity;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_ACTIVATE_SCENE_CFM:
                switch (data[FIRSTBYTE]) {
                    case 0:
                        logger.debug("Request to execute scene accepted");
                        break;
                    case 1:
                        logger.error("Request to execute scene rejected - Invalid Parameter");
                        this.setStatus(CommandStatus.ERROR
                                .setErrorDetail("Request to execute scene rejected - Invalid Parameter"));
                        break;
                    case 2:
                        logger.error("Request to execute scene rejected - Request Rejected");
                        this.setStatus(CommandStatus.ERROR
                                .setErrorDetail("Request to execute scene rejected - Invalid Parameter"));
                        break;
                }
                return true;
            case GW_SESSION_FINISHED_NTF:
                logger.debug("Finished executing scene");
                this.setStatus(CommandStatus.COMPLETE);
                return true;
            case GW_NODE_STATE_POSITION_CHANGED_NTF:
                logger.trace(
                        "Node {} position changed, state: {}, current position: {}, target position:{}, time remaining: {} seconds.",
                        data[FIRSTBYTE], VeluxState.createFromCode(data[FIRSTBYTE + 1]),
                        VeluxPosition.createFromCode(KLFUtils.extractTwoBytes(data, FIRSTBYTE + 2)).toString(),
                        VeluxPosition.createFromCode(KLFUtils.extractTwoBytes(data, FIRSTBYTE + 4)).toString(),
                        KLFUtils.extractTwoBytes(data, FIRSTBYTE + 14));
                return true;
            case GW_COMMAND_RUN_STATUS_NTF:
                VeluxRunStatus runStatus = VeluxRunStatus.createFromCode(data[FIRSTBYTE + 7]);
                StatusReply statusReply = StatusReply.fromCode(data[FIRSTBYTE + 8]);
                logger.trace(
                        "Notification for Node {}, relating to function parameter {}, Run status is: {}, Command status is: {} ",
                        data[FIRSTBYTE + 3], data[FIRSTBYTE + 4], runStatus, statusReply);
                return true;
            case GW_COMMAND_REMAINING_TIME_NTF:
                logger.trace(
                        "Notification for Node {}, relating to function parameter {}, time remaining to complete is {} seconds",
                        data[FIRSTBYTE + 2], data[FIRSTBYTE + 3], KLFUtils.extractTwoBytes(data, FIRSTBYTE + 4));
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.ACTIVATE_SCENE;
    }

    @Override
    protected byte[] pack() {
        byte[] data = new byte[6];
        data[0] = (byte) (this.getSessionID() >>> 8);
        data[1] = (byte) this.getSessionID();
        data[2] = CMD_ORIGINATOR_USER;
        data[3] = CMD_PRIORITY_NORMAL;
        data[4] = this.sceneId;
        data[5] = this.velocity.getCode();
        return data;
    }

    @Override
    protected int extractSession(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SESSION_FINISHED_NTF:
            case GW_COMMAND_REMAINING_TIME_NTF:
            case GW_COMMAND_RUN_STATUS_NTF:
                return KLFUtils.extractTwoBytes(data, FIRSTBYTE);
            case GW_NODE_STATE_POSITION_CHANGED_NTF:
                // This command does not include a session parameter, so just return
                // our own current session ID instead
                return this.getSessionID();
            case GW_ACTIVATE_SCENE_CFM:
            default:
                return KLFUtils.extractTwoBytes(data, FIRSTBYTE + 1);
        }
    }

    @Override
    public boolean isSessionRequired() {
        return true;
    }

    @Override
    public boolean isNodeSpecific() {
        return false;
    }

    @Override
    public KLFGatewayCommands getCommand() {
        return KLFGatewayCommands.GW_ACTIVATE_SCENE_REQ;
    }
}