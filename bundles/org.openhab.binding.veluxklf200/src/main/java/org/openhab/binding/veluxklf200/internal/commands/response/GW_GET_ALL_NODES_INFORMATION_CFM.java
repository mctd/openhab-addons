package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GetAllNodesStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_ALL_NODES_INFORMATION_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_ALL_NODES_INFORMATION_CFM.class);

    private GetAllNodesStatus status;
    private int totalNumberOfNodes;

    public GW_GET_ALL_NODES_INFORMATION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = GetAllNodesStatus.fromCode(this.getCommandFrame().readByte(1));
        this.totalNumberOfNodes = this.getCommandFrame().readByteAsInt(2);

        logger.debug("Get All Nodes result: {}, total number of nodes: {}", this.status, this.totalNumberOfNodes);
    }

    public GetAllNodesStatus getStatus() {
        return status;
    }

    public int getTotalNumberOfNodes() {
        return totalNumberOfNodes;
    }
}
