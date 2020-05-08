package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_HOUSE_STATUS_MONITOR_ENABLE_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_HOUSE_STATUS_MONITOR_ENABLE_REQ extends BaseRequest<GW_HOUSE_STATUS_MONITOR_ENABLE_CFM> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GW_HOUSE_STATUS_MONITOR_ENABLE_REQ() {
        super(GatewayCommands.GW_HOUSE_STATUS_MONITOR_ENABLE_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_HOUSE_STATUS_MONITOR_ENABLE_CFM response) {
        logger.debug("House Status Monitor service successfully enabled.");
        return true;
    }
}
