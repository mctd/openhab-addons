package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;

@NonNullByDefault
public abstract class BaseConfirmationResponse extends BaseResponse {

    public BaseConfirmationResponse(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
    }
}
