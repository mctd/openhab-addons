package org.openhab.binding.veluxklf200.internal.handler;

import static org.openhab.binding.veluxklf200.internal.VeluxKlf200BindingConstants.CHANNEL_CONTROL;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_NODE_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_NODE_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_NODE_INFORMATION_NTF;
import org.openhab.binding.veluxklf200.internal.commands.status.ExecutionState;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeInformationStatus;
import org.openhab.binding.veluxklf200.internal.events.EventBroker;
import org.openhab.binding.veluxklf200.internal.events.NodeEvent;
import org.openhab.binding.veluxklf200.internal.events.NodeEventListener;
import org.openhab.binding.veluxklf200.internal.events.NodePositionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public abstract class VeluxKlf200BaseNodeHandler extends VeluxKlf200BaseThingHandler implements NodeEventListener {

    private final Logger logger = LoggerFactory.getLogger(VeluxKlf200BaseNodeHandler.class);
    private int nodeId;

    public VeluxKlf200BaseNodeHandler(Thing thing) {
        super(thing);
        this.nodeId = Integer.valueOf(thing.getUID().getId());
    }

    @Override
    public void initialize() {
        EventBroker.addListener(this);
        updateStatus(ThingStatus.UNKNOWN);
    }

    @Override
    public void dispose() {
        EventBroker.removeListener(this);
        super.dispose();
    }

    @Override
    public int getListenedNodeId() {
        return this.nodeId;
    }

    protected void refreshNodeInfo() {
        // Send a Node Information Request. The result will be sent back as a notification message.
        GW_GET_NODE_INFORMATION_REQ getNodeInfoReq = new GW_GET_NODE_INFORMATION_REQ(this.nodeId);
        sendRequest(getNodeInfoReq);
        GW_GET_NODE_INFORMATION_CFM response = getNodeInfoReq.getResponse();

        if (response != null) {
            if (response.getStatus() == NodeInformationStatus.OK) {
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
    public void handleEvent(NodeEvent event) {
        logger.debug("Handler for {} is handling event {}", this.getThing().getUID(), event);

        // Handles GW_NODE_STATE_POSITION_CHANGED_NTF and position part of GW_GET_NODE_INFORMATION_NTF
        if (event instanceof NodePositionEvent) {
            NodePositionEvent posEvent = (NodePositionEvent) event;

            // Only process event when state is "Done" or "Error"
            if (posEvent.getExecutionState() == ExecutionState.DONE
                    || posEvent.getExecutionState() == ExecutionState.ERROR_EXECUTING) {
                Integer position = posEvent.getCurrentPosition().getPosition();
                State itemState = position != null ? new PercentType(position) : UnDefType.UNDEF;

                Channel channel = this.getThing().getChannel(CHANNEL_CONTROL);
                if (channel != null) {
                    this.updateState(channel.getUID(), itemState);
                }

            } else {
                logger.trace("Ignoring event {} which Execution State is {}", posEvent, posEvent.getExecutionState());
            }
        }

        if (event instanceof GW_GET_NODE_INFORMATION_NTF) {
            GW_GET_NODE_INFORMATION_NTF nodeInfoEvent = (GW_GET_NODE_INFORMATION_NTF) event;
            Map<String, String> properties = this.editProperties();
            properties.put("Power Mode", nodeInfoEvent.getPowerMode().toString());
            properties.put("Node Variation", nodeInfoEvent.getNodeVariation().toString());
            properties.put("Product Group", String.valueOf(nodeInfoEvent.getProductGroup()));
            properties.put("Serial Number", Long.toUnsignedString(nodeInfoEvent.getSerialNumber()));
            properties.put("Velocity", nodeInfoEvent.getVelocity().toString());
            properties.put("Placement", String.valueOf(nodeInfoEvent.getPlacement()));
            properties.put("Order", String.valueOf(nodeInfoEvent.getOrder()));
            properties.put("Node Type SubType", nodeInfoEvent.getNodeTypeSubType().toString());
            properties.put("Product Type", nodeInfoEvent.getProductType().toString());
            properties.put("Build Number", String.valueOf(nodeInfoEvent.getBuildNumber()));
            properties.put("Execution State", nodeInfoEvent.getExecutionState().toString());
            properties.put("NbrOfAlias", Byte.toString(nodeInfoEvent.getNbrOfAlias()));
            this.updateProperties(properties);

            logger.debug("Properties of thing {} updated to {}", this.getThing().getUID(), properties);
        }
    }
}
