package org.openhab.binding.veluxklf200.internal.commands.request;

import java.time.Clock;
import java.time.Instant;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_SET_UTC_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request to set UTC time.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_SET_UTC_REQ extends BaseRequest<GW_SET_UTC_CFM> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GW_SET_UTC_REQ() {
        super(GatewayCommands.GW_SET_UTC_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        long theTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        commandFrame.writeInt((int) theTime);
    }

    @Override
    public boolean handleResponseImpl(GW_SET_UTC_CFM response) {
        logger.debug("Set time successful.");
        return true;
    }
}
