package org.openhab.binding.veluxklf200.internal.events;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.ThingUID;

public interface BaseEventListener<T extends BaseEvent> {
    ThingUID getBridgeUID();

    void handleEvent(@NonNull T event);
}
