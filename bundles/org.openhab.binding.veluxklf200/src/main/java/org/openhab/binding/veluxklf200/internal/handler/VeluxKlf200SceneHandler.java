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
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200BindingConstants;
import org.openhab.binding.veluxklf200.internal.commands.request.BaseRequest;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_ACTIVATE_SCENE_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_STOP_SCENE_REQ;
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
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void dispose() {
    }

    private int getSceneId() {
        return Integer.valueOf(this.getThing().getUID().getId());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling scene command: {}.", command);

        if (!VeluxKlf200BindingConstants.CHANNEL_TRIGGER_SCENE.equals(channelUID.getId())) {
            return;
        }

        if (command == RefreshType.REFRESH) {

        } else if (command instanceof OnOffType) {
            BaseRequest<?> request;
            if (command == OnOffType.ON) {
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
    }
}