package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GetNodeInformationStatus;

@NonNullByDefault
public class GW_GET_NODE_INFORMATION_CFM extends BaseConfirmationResponse {
    private GetNodeInformationStatus status;
    private short nodeId;

    public GW_GET_NODE_INFORMATION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = GetNodeInformationStatus.fromCode(this.getCommandFrame().readByte(1));
        this.nodeId = this.getCommandFrame().readByte(2);
    }

    public GetNodeInformationStatus getStatus() {
        return this.status;
    }

    public short getNodeId() {
        return this.nodeId;
    }
}
