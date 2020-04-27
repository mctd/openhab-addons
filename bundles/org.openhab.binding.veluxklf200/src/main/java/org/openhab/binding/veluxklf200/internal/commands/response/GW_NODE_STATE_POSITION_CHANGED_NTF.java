package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.Position;
import org.openhab.binding.veluxklf200.internal.status.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_NODE_STATE_POSITION_CHANGED_NTF extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_NODE_STATE_POSITION_CHANGED_NTF.class);

    private byte nodeID;
    private State state;
    private Position currentPosition;
    private Position target;
    private Position fp1currentPosition;
    private Position fp2currentPosition;
    private Position fp3currentPosition;
    private Position fp4currentPosition;
    private short remainingTime;
    private int timeStamp;

    public GW_NODE_STATE_POSITION_CHANGED_NTF(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.nodeID = this.getCommandFrame().readByte(1);
        this.state = State.fromCode(this.getCommandFrame().readByte(2));
        this.currentPosition = Position.fromCode(this.getCommandFrame().readShort(3));
        this.target = Position.fromCode(this.getCommandFrame().readShort(5));
        this.fp1currentPosition = Position.fromCode(this.getCommandFrame().readShort(7));
        this.fp2currentPosition = Position.fromCode(this.getCommandFrame().readShort(9));
        this.fp3currentPosition = Position.fromCode(this.getCommandFrame().readShort(11));
        this.fp4currentPosition = Position.fromCode(this.getCommandFrame().readShort(13));
        this.remainingTime = this.getCommandFrame().readShort(15);
        this.timeStamp = this.getCommandFrame().readInt(17);

        logger.info(
                "GW_GET_NODE_INFORMATION_NTF: nodeID: {}, state: {}, currentPosition: {}, target: {}, fp1currentPosition: {}, "
                        + "fp2currentPosition: {}, fp3currentPosition: {}, fp4currentPosition: {}, remainingTime: {}, timeStamp: {}",
                this.nodeID, this.state, this.currentPosition, this.target, this.fp1currentPosition,
                this.fp2currentPosition, this.fp3currentPosition, this.fp4currentPosition, this.remainingTime,
                this.timeStamp);

        // TODO : update position channel : fire event
    }

    public short getNodeID() {
        return this.nodeID;
    }
}
