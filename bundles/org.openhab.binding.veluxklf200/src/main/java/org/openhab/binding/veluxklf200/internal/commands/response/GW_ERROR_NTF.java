package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.ErrorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response sent by KLF200 unit when an error occurred.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_ERROR_NTF extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_ERROR_NTF.class);

    private ErrorState errorState;

    public GW_ERROR_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.errorState = ErrorState.fromCode(this.getCommandFrame().readByte(1));

        logger.debug("GW_ERROR_NTF: error with state: {}.", this.errorState);
    }

    public ErrorState getErrorState() {
        return this.errorState;
    }
}
