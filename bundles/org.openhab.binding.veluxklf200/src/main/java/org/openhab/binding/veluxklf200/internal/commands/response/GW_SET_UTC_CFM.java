package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_SET_UTC_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_SET_UTC_CFM.class);

    public GW_SET_UTC_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);

        logger.info("Set time successful.");
    }
}
