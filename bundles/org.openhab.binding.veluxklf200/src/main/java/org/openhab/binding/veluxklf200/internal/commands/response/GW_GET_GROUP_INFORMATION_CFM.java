package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GetAllGroupsStatus;

@NonNullByDefault
public class GW_GET_GROUP_INFORMATION_CFM extends BaseConfirmationResponse {
    private GetAllGroupsStatus status;
    private short groupId;

    public GW_GET_GROUP_INFORMATION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = GetAllGroupsStatus.fromCode(this.getCommandFrame().readByte(1));
        this.groupId = this.getCommandFrame().readByte(2);
    }

    public GetAllGroupsStatus getStatus() {
        return this.status;
    }

    public short getGroupId() {
        return this.groupId;
    }
}
