package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_REBOOT_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_REBOOT_REQ extends BaseRequest<GW_REBOOT_CFM> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public GW_REBOOT_REQ() {
        super(GatewayCommands.GW_REBOOT_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_REBOOT_CFM response) {
        logger.warn("Reboot accepted, connection to KLF must be restarted.");
        return true;
    }
}
