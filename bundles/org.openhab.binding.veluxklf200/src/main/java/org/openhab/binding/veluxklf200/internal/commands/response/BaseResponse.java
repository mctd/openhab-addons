package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;

@NonNullByDefault
public abstract class BaseResponse {
    private ThingUID bridgeUID;
    private KLFCommandFrame commandFrame;

    public BaseResponse(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        this.commandFrame = commandFrame;
        this.bridgeUID = bridgeUID;
    }

    protected final KLFCommandFrame getCommandFrame() {
        return this.commandFrame;
    }

    public final ThingUID getBridgeUID() {
        return this.bridgeUID;
    }
}
