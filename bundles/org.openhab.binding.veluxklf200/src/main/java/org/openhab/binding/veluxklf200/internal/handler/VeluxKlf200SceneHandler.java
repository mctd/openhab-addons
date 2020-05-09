/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200BindingConstants;
import org.openhab.binding.veluxklf200.internal.commands.request.BaseRequest;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_ACTIVATE_SCENE_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_SCENE_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_STOP_SCENE_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_SCENE_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.status.GetSceneInformationStatus;
import org.openhab.binding.veluxklf200.internal.commands.status.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles Scene Things.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200SceneHandler extends VeluxKlf200BaseThingHandler {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(VeluxKlf200SceneHandler.class);

    /**
     * Constructor
     *
     * @param thing the thing
     */
    public VeluxKlf200SceneHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);
    }

    @Override
    public void dispose() {
    }

    private int getSceneId() {
        return Integer.valueOf(this.getThing().getUID().getId());
    }

    private void refreshSceneInfo() {
        // Send a Scene Information Request. The result will be sent back as a notification message.
        GW_GET_SCENE_INFORMATION_REQ getSceneInfoReq = new GW_GET_SCENE_INFORMATION_REQ(this.getSceneId());
        sendRequest(getSceneInfoReq);
        GW_GET_SCENE_INFORMATION_CFM response = getSceneInfoReq.getResponse();

        if (response != null) {
            if (response.getStatus() == GetSceneInformationStatus.OK) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        response.getStatus().toString());
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "No response.");
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling scene command: {}.", command);

        if (!VeluxKlf200BindingConstants.CHANNEL_TRIGGER_SCENE.equals(channelUID.getId())) {
            return;
        }

        if (command == RefreshType.REFRESH) {
            refreshSceneInfo();
        } else if (command instanceof OnOffType) {
            BaseRequest<?> request;
            if (command == OnOffType.ON) {
                // TODO : make velocity a configuration ? or a channel !?
                request = new GW_ACTIVATE_SCENE_REQ(this.getSceneId(), Velocity.DEFAULT);
            } else {
                request = new GW_STOP_SCENE_REQ(this.getSceneId());
            }
            sendRequest(request);

        } else {
            logger.warn("Command not understood: {}", command);
        }

        // TODO : refresh ==> GW_GET_SCENE_INFOAMATION_REQ
        // TODO : Listen to GW_SESSION_FINISHED_NTF with session ID to mark it off again
        // Problem : the sendCmd is synchronized, so it will block ? maybe not as we call wait() on command ?
    }
}