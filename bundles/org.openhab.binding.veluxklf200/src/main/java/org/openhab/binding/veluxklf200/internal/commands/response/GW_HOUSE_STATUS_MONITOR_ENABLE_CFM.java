package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_HOUSE_STATUS_MONITOR_ENABLE_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_HOUSE_STATUS_MONITOR_ENABLE_CFM.class);

    public GW_HOUSE_STATUS_MONITOR_ENABLE_CFM(KLFCommandFrame commandFrame) {
        super(commandFrame);

        logger.info("House Status Monitor service successfully enabled.");
    }
}
