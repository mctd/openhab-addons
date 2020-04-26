package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.NodeInformationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_GET_NODE_INFORMATION_CFM extends BaseResponse {

    private static final Logger logger = LoggerFactory.getLogger(GW_GET_NODE_INFORMATION_CFM.class);

    private NodeInformationStatus status;
    private short nodeID;

    public GW_GET_NODE_INFORMATION_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.status = NodeInformationStatus.fromCode(this.getCommandFrame().getByte(1));
        this.nodeID = this.getCommandFrame().getByte(2);

        logger.info("Get Node Information status: {}, nodeID: {}", this.getStatus(), this.getNodeID());
    }

    public NodeInformationStatus getStatus() {
        return this.status;
    }

    public short getNodeID() {
        return this.nodeID;
    }
}
