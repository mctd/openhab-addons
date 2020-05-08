package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_GROUP_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request information about all defined groups.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_GROUP_INFORMATION_REQ extends BaseRequest<GW_GET_GROUP_INFORMATION_CFM> {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_GROUP_INFORMATION_REQ.class);
    private int groupId;

    public GW_GET_GROUP_INFORMATION_REQ(int groupId) {
        super(GatewayCommands.GW_GET_GROUP_INFORMATION_REQ);
        this.groupId = groupId;
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        commandFrame.writeByte((byte) (this.groupId & 0xFF));
    }

    @Override
    public boolean handleResponseImpl(GW_GET_GROUP_INFORMATION_CFM response) {
        logger.debug("Get Group Information status: {}, groupId: {}", response.getStatus(), response.getGroupId());
        return true;
    }
}
