package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_NODE_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request extended information of one specific actuator node.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_NODE_INFORMATION_REQ extends BaseRequest<GW_GET_NODE_INFORMATION_CFM> {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_NODE_INFORMATION_REQ.class);
    private int nodeId;

    public GW_GET_NODE_INFORMATION_REQ(int nodeId) {
        super(GatewayCommands.GW_GET_NODE_INFORMATION_REQ);
        this.nodeId = nodeId;
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        commandFrame.writeByte((byte) (this.nodeId & 0xFF));
    }

    @Override
    public boolean handleResponseImpl(GW_GET_NODE_INFORMATION_CFM response) {
        logger.debug("Get Node Information status: {}, nodeID: {}", response.getStatus(), response.getNodeId());
        return true;
    }
}
