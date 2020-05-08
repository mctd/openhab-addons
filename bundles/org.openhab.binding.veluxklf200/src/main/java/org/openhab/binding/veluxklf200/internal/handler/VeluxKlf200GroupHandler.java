/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_GROUP_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_GROUP_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_GROUP_INFORMATION_NTF;
import org.openhab.binding.veluxklf200.internal.commands.status.GetAllGroupsStatus;
import org.openhab.binding.veluxklf200.internal.events.EventBroker;
import org.openhab.binding.veluxklf200.internal.events.GroupEvent;
import org.openhab.binding.veluxklf200.internal.events.GroupEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles Groups
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200GroupHandler extends VeluxKlf200BaseThingHandler implements GroupEventListener {
    private final Logger logger = LoggerFactory.getLogger(VeluxKlf200GroupHandler.class);
    private int groupId;

    /**
     * Constructor
     *
     * @param thing Thing to Handle
     */
    public VeluxKlf200GroupHandler(Thing thing) {
        super(thing);
        this.groupId = Integer.valueOf(thing.getUID().getId());
    }

    @Override
    public void initialize() {
        EventBroker.addListener(this);

    }

    @Override
    public void dispose() {
        EventBroker.removeListener(this);
        super.dispose();
    }

    private void refreshState() {
        GW_GET_GROUP_INFORMATION_REQ getGroupInfoReq = new GW_GET_GROUP_INFORMATION_REQ(this.groupId);
        sendRequest(getGroupInfoReq);
        GW_GET_GROUP_INFORMATION_CFM response = getGroupInfoReq.getResponse();

        if (response != null) {
            if (response.getStatus() == GetAllGroupsStatus.OK) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        response.getStatus().toString());
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "No response");
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.warn("Not implemented.");

        if (command == RefreshType.REFRESH) {
            refreshState();
        }

        // TODO : send GW_ACTIVATE_PRODUCTGROUP_REQ or GET GROUP INFO if REFRESH
    }

    @Override
    public int getListenedGroupId() {
        return this.groupId;
    }

    @Override
    public void handleEvent(@NonNull GroupEvent event) {
        if (event instanceof GW_GET_GROUP_INFORMATION_NTF) {
            GW_GET_GROUP_INFORMATION_NTF groupInfoEvent = (GW_GET_GROUP_INFORMATION_NTF) event;
            Map<String, String> properties = this.editProperties();
            properties.put("Order", String.valueOf(groupInfoEvent.getOrder()));
            properties.put("Placement", String.valueOf(groupInfoEvent.getPlacement()));
            properties.put("Velocity", groupInfoEvent.getVelocity().toString());
            properties.put("Node Variation", groupInfoEvent.getNodeVariation().toString());
            properties.put("Group Type", groupInfoEvent.getGroupType().toString());
            properties.put("NbrOfObjects", String.valueOf(groupInfoEvent.getNbrOfObjects()));
            properties.put("Actuators", groupInfoEvent.getActuators().toString());
            properties.put("Revision", Short.toString(groupInfoEvent.getRevision()));
            this.updateProperties(properties);
        }
    }
}