package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_GROUPS_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.status.GroupType;

@NonNullByDefault
public class GW_GET_ALL_GROUPS_INFORMATION_REQ extends BaseRequest<GW_GET_ALL_GROUPS_INFORMATION_CFM> {
    private @Nullable GroupType groupType;

    /**
     * Request to receive all groups.
     *
     * @param groupType Group type to filter on. Null if no filter.
     */
    public GW_GET_ALL_GROUPS_INFORMATION_REQ(@Nullable GroupType groupType) {
        super(GatewayCommands.GW_GET_ALL_GROUPS_INFORMATION_REQ);
        this.groupType = groupType;
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // TODO : debug this command (seems to return filtered results in any case)
        commandFrame.writeByte(this.groupType == null ? (byte) 0 : (byte) 1); // UseFilter: 0 for all groups
        commandFrame.writeByte(groupType != null ? groupType.getCode() : 1); // GroupType filter if UseFilter !=0
    }

    @Override
    public boolean handleResponseImpl(GW_GET_ALL_GROUPS_INFORMATION_CFM response) {
        return true;
    }
}
