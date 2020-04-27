package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.NodeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This command tells how long it takes until the actuator has reached the desired position.
 *
 * @author emmanuel
 *
 */
public class GW_COMMAND_REMAINING_TIME_NTF extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_REMAINING_TIME_NTF.class);

    private short sessionID;
    private byte index;
    private NodeParameter nodeParameter;
    private short timeRemaining;

    public GW_COMMAND_REMAINING_TIME_NTF(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.sessionID = this.getCommandFrame().readShort(1);
        this.index = this.getCommandFrame().readByte(3);
        this.nodeParameter = NodeParameter.fromCode(this.getCommandFrame().readByte(4));
        this.timeRemaining = this.getCommandFrame().readShort(5);

        logger.info(
                "GW_COMMAND_REMAINING_TIME_NTF: Command with sessionID: {}, for nodeID: {}, nodeParameter: {}, will end in {} seconds.",
                this.getSessionID(), this.index, this.nodeParameter, this.timeRemaining);
    }

    public short getSessionID() {
        return this.sessionID;
    }
}
