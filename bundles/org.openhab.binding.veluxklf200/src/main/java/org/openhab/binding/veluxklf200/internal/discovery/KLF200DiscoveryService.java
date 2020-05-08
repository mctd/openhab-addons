/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.discovery;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200BindingConstants;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_ALL_GROUPS_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_ALL_NODES_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_SCENE_LIST_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_GROUPS_INFORMATION_NTF;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_NODES_INFORMATION_NTF;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_SCENE_LIST_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_SCENE_LIST_NTF;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeTypeSubType;
import org.openhab.binding.veluxklf200.internal.events.BridgeEvent;
import org.openhab.binding.veluxklf200.internal.events.BridgeEventListener;
import org.openhab.binding.veluxklf200.internal.events.EventBroker;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200BridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovers all nodes, scenes and groups registered on KLF200 unit.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class KLF200DiscoveryService extends AbstractDiscoveryService implements BridgeEventListener {
    private static final int DISCOVERY_TIMEOUT = 10;
    private Logger logger = LoggerFactory.getLogger(KLF200DiscoveryService.class);
    private VeluxKlf200BridgeHandler bridgeHandler;
    private boolean nodesDiscoveryComplete = false;
    private boolean groupsDiscoveryComplete = false;
    private boolean scenesDiscoveryComplete = false;

    public KLF200DiscoveryService(VeluxKlf200BridgeHandler bridge) {
        super(VeluxKlf200BindingConstants.SUPPORTED_VELUX_KLF200_THING_TYPES_UIDS, DISCOVERY_TIMEOUT, true);
        logger.debug("KLF200DiscoveryService {}", bridge);
        this.bridgeHandler = bridge;
    }

    @Override
    public ThingUID getBridgeUID() {
        return this.bridgeHandler.getThing().getUID();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        removeOlderResults(new Date().getTime());
    }

    @Override
    protected void startScan() {
        // Reset completion marks before start
        this.nodesDiscoveryComplete = false;
        this.groupsDiscoveryComplete = false;
        this.scenesDiscoveryComplete = false;

        EventBroker.addListener(this);

        discoverNodes();
        discoverGroups();
        discoverScenes();
    }

    @Override
    protected synchronized void stopScan() {
        EventBroker.removeListener(this);
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());

        if (!this.nodesDiscoveryComplete) {
            logger.warn("Discovery stopped but nodes discovery did not complete.");
        }
        if (!this.groupsDiscoveryComplete) {
            logger.warn("Discovery stopped but groups discovery did not complete.");
        }
        if (!this.scenesDiscoveryComplete) {
            logger.warn("Discovery stopped but scenes discovery did not complete.");
        }

        if (this.groupsDiscoveryComplete && this.nodesDiscoveryComplete && this.scenesDiscoveryComplete) {
            logger.debug("Discovery stopped, everything completed.");
        }
    }

    @Override
    public synchronized void abortScan() {
        EventBroker.removeListener(this);
        super.abortScan();
    }

    private void discoverNodes() {
        GW_GET_ALL_NODES_INFORMATION_REQ getAllNodesReq = new GW_GET_ALL_NODES_INFORMATION_REQ();
        this.bridgeHandler.getConnection().sendRequest(getAllNodesReq);
    }

    private void discoverGroups() {
        GW_GET_ALL_GROUPS_INFORMATION_REQ getAllGroupsReq = new GW_GET_ALL_GROUPS_INFORMATION_REQ(null);
        this.bridgeHandler.getConnection().sendRequest(getAllGroupsReq);
    }

    private void discoverScenes() {
        GW_GET_SCENE_LIST_REQ getSceneListReq = new GW_GET_SCENE_LIST_REQ();
        this.bridgeHandler.getConnection().sendRequest(getSceneListReq);

        GW_GET_SCENE_LIST_CFM response = getSceneListReq.getResponse();
        if (response != null && response.getTotalNumberOfScenes() == 0) {
            // Command completed and no scenes defined, KLF won't send NTF message
            this.scenesDiscoveryComplete = true;
        }
    }

    private @Nullable DiscoveryResult handleNode(GW_GET_ALL_NODES_INFORMATION_NTF event) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("Power Mode", event.getPowerMode());
        properties.put("Node Variation", event.getNodeVariation());
        properties.put("Product Group", event.getProductGroup());
        properties.put("Serial Number", Long.toUnsignedString(event.getSerialNumber()));
        properties.put("Velocity", event.getVelocity().toString());
        properties.put("Placement", event.getPlacement());
        properties.put("Order", event.getOrder());
        properties.put("Build Number", event.getBuildNumber());
        properties.put("Product Type", event.getProductType());
        properties.put("TimeStamp", event.getTimeStamp());

        ThingTypeUID thingTypeUID;
        switch (event.getNodeTypeSubType().getNodeType()) {
            case ROLLER_SHUTTER:
                thingTypeUID = VeluxKlf200BindingConstants.THING_TYPE_ROLLER_SHUTTER;
                break;
            case WINDOW_OPENER:
                thingTypeUID = VeluxKlf200BindingConstants.THING_TYPE_WINDOW_OPENER;
            default:
                logger.warn("{} is not yet implemented, using a generic actuator handler",
                        event.getNodeTypeSubType().getNodeType());
                thingTypeUID = VeluxKlf200BindingConstants.THING_TYPE_ACTUATOR;
                break;
        }

        String thingLabel = event.getName();

        // In case the node doesn't have a name on KLF unit, this generates a default label
        if (thingLabel.isEmpty()) {
            if (event.getNodeTypeSubType() == NodeTypeSubType.UNKNOWN) {
                thingLabel = String.format("Unkown node #%d" + event.getNodeId());
            } else {
                thingLabel = event.getNodeTypeSubType() + " #" + event.getNodeId();
            }
        }

        logger.debug("Found node \"{}\" with id: {}", event.getName(), event.getNodeId());

        ThingUID thingUID = new ThingUID(thingTypeUID, this.getBridgeUID(), String.valueOf(event.getNodeId()));
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                .withBridge(this.getBridgeUID()).withLabel(thingLabel).build();
        return discoveryResult;
    }

    private DiscoveryResult handleGroup(GW_GET_ALL_GROUPS_INFORMATION_NTF event) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("Order", event.getOrder());
        properties.put("Placement", event.getPlacement());
        properties.put("Velocity", event.getVelocity().toString());
        properties.put("Node Variation", event.getNodeVariation());
        properties.put("Group Type", event.getGroupType());
        properties.put("NbrOfObjects", event.getNbrOfObjects());
        properties.put("Actuators", event.getActuatorBitArray());
        properties.put("Revision", event.getRevision());

        logger.debug("Found group \"{}\" with id: {}", event.getName(), event.getGroupId());

        ThingUID thingUID = new ThingUID(VeluxKlf200BindingConstants.THING_TYPE_GROUP, this.getBridgeUID(),
                String.valueOf(event.getGroupId()));
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                .withBridge(this.getBridgeUID()).withLabel(event.getName()).build();
        return discoveryResult;
    }

    private void handleScene(GW_GET_SCENE_LIST_NTF event) {
        for (GW_GET_SCENE_LIST_NTF.SceneDescription sceneDesc : event.getScenes()) {
            logger.debug("Found scene \"{}\" with id: {}", sceneDesc.getName(), sceneDesc.getId());

            ThingUID thingUID = new ThingUID(VeluxKlf200BindingConstants.THING_TYPE_SCENE, this.getBridgeUID(),
                    Integer.toString(sceneDesc.getId()));
            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withBridge(this.getBridgeUID())
                    .withLabel(sceneDesc.getName()).build();
            thingDiscovered(discoveryResult);

            // mark scene discovery as complete
            if (event.getRemainingNumberOfObject() == 0) {
                this.scenesDiscoveryComplete = true;
            }
        }
    }

    @Override
    public void handleEvent(BridgeEvent event) {
        DiscoveryResult discoveryResult = null;
        if (event instanceof GW_GET_ALL_NODES_INFORMATION_NTF) {
            GW_GET_ALL_NODES_INFORMATION_NTF nodeInfoEvent = (GW_GET_ALL_NODES_INFORMATION_NTF) event;
            discoveryResult = handleNode(nodeInfoEvent);
        } else if (event instanceof GW_GET_ALL_GROUPS_INFORMATION_NTF) {
            GW_GET_ALL_GROUPS_INFORMATION_NTF groupInfoEvent = (GW_GET_ALL_GROUPS_INFORMATION_NTF) event;
            discoveryResult = handleGroup(groupInfoEvent);
        } else if (event instanceof GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF) {
            // mark nodes discovery as complete
            this.nodesDiscoveryComplete = true;
        } else if (event instanceof GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF) {
            // mark groups discovery as complete
            this.groupsDiscoveryComplete = true;
        } else if (event instanceof GW_GET_SCENE_LIST_NTF) {
            // Scene handling is a bit different as a single NTF message can contain multiple scenes description
            handleScene((GW_GET_SCENE_LIST_NTF) event);
        } else {
            // Ignore event
            logger.debug("Ignoring event: {}", event);
        }

        if (discoveryResult != null) {
            thingDiscovered(discoveryResult);
        }
    }
}