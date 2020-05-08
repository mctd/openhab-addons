package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.events.NodeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This command tells how long it takes until the actuator has reached the desired position.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_COMMAND_REMAINING_TIME_NTF extends BaseNotificationResponse implements NodeEvent {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_REMAINING_TIME_NTF.class);

    private int sessionID;
    private int nodeId;
    private NodeParameter nodeParameter;
    private int timeRemaining;

    public GW_COMMAND_REMAINING_TIME_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.sessionID = this.getCommandFrame().readShortAsInt(1);
        this.nodeId = this.getCommandFrame().readByteAsInt(3);
        this.nodeParameter = NodeParameter.fromCode(this.getCommandFrame().readByte(4));
        this.timeRemaining = this.getCommandFrame().readShortAsInt(5);

        logger.debug("Command with sessionID: {}, for nodeID: {}, nodeParameter: {}, will end in {} seconds.",
                this.sessionID, this.nodeId, this.nodeParameter, this.timeRemaining);
    }

    @Override
    public int getNodeId() {
        return this.nodeId;
    }
}
