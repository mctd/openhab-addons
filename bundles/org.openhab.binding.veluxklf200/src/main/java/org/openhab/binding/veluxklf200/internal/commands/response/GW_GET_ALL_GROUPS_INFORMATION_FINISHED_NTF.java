package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.events.BridgeEvent;

/**
 * This event is sent after the last group information, indicating no more groups.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF extends BaseNotificationResponse implements BridgeEvent {
    public GW_GET_ALL_GROUPS_INFORMATION_FINISHED_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
    }
}
