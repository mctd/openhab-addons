package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.events.BaseEvent;

@NonNullByDefault
public abstract class BaseNotificationResponse extends BaseResponse implements BaseEvent {

    public BaseNotificationResponse(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
    }
}
