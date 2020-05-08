package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.ExecutionState;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;
import org.openhab.binding.veluxklf200.internal.events.NodePositionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If House Status Monitor has been enabled then GW_NODE_STATE_POSITION_CHANGED_NTF will be send when somebody change
 * state or position on a known actuator
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_NODE_STATE_POSITION_CHANGED_NTF extends BaseNotificationResponse implements NodePositionEvent {
    private static final Logger logger = LoggerFactory.getLogger(GW_NODE_STATE_POSITION_CHANGED_NTF.class);

    private int nodeId;
    private ExecutionState executionState;
    private Position currentPosition;
    private Position target;
    private Position fp1currentPosition;
    private Position fp2currentPosition;
    private Position fp3currentPosition;
    private Position fp4currentPosition;
    private int remainingTime;
    private int timeStamp;

    public GW_NODE_STATE_POSITION_CHANGED_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.nodeId = this.getCommandFrame().readByteAsInt(1);
        this.executionState = ExecutionState.fromCode(this.getCommandFrame().readByte(2));
        this.currentPosition = Position.fromCode(this.getCommandFrame().readShort(3));
        this.target = Position.fromCode(this.getCommandFrame().readShort(5));
        this.fp1currentPosition = Position.fromCode(this.getCommandFrame().readShort(7));
        this.fp2currentPosition = Position.fromCode(this.getCommandFrame().readShort(9));
        this.fp3currentPosition = Position.fromCode(this.getCommandFrame().readShort(11));
        this.fp4currentPosition = Position.fromCode(this.getCommandFrame().readShort(13));
        this.remainingTime = this.getCommandFrame().readShortAsInt(15);
        this.timeStamp = this.getCommandFrame().readInt(17);

        logger.debug(
                "GW_NODE_STATE_POSITION_CHANGED_NTF: nodeId: {}, executionState: {}, currentPosition: {}, target: {}, fp1currentPosition: {}, "
                        + "fp2currentPosition: {}, fp3currentPosition: {}, fp4currentPosition: {}, remainingTime: {}, timeStamp: {}",
                this.nodeId, this.getExecutionState(), this.currentPosition, this.target, this.fp1currentPosition,
                this.fp2currentPosition, this.fp3currentPosition, this.fp4currentPosition, this.remainingTime,
                this.timeStamp);
    }

    @Override
    public int getNodeId() {
        return this.nodeId;
    }

    @Override
    public ExecutionState getExecutionState() {
        return executionState;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Position getTarget() {
        return target;
    }

    @Override
    public Position getFp1currentPosition() {
        return fp1currentPosition;
    }

    @Override
    public Position getFp2currentPosition() {
        return fp2currentPosition;
    }

    @Override
    public Position getFp3currentPosition() {
        return fp3currentPosition;
    }

    @Override
    public Position getFp4currentPosition() {
        return fp4currentPosition;
    }

    @Override
    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public int getTimeStamp() {
        return timeStamp;
    }
}
