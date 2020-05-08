package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GetAllGroupsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_ALL_GROUPS_INFORMATION_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_ALL_GROUPS_INFORMATION_CFM.class);

    private GetAllGroupsStatus status;
    private int totalNumberOfGroups;

    public GW_GET_ALL_GROUPS_INFORMATION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = GetAllGroupsStatus.fromCode(commandFrame.readByte(1));
        this.totalNumberOfGroups = commandFrame.readByteAsInt(2);

        logger.debug("Get All Groups result: {}, total number of groups: {}", this.status, this.totalNumberOfGroups);
    }

    public GetAllGroupsStatus getStatus() {
        return status;
    }

    public int getTotalNumberOfGroups() {
        return totalNumberOfGroups;
    }
}
