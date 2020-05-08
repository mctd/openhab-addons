package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;

@NonNullByDefault
public class GW_HOUSE_STATUS_MONITOR_ENABLE_CFM extends BaseConfirmationResponse {
    public GW_HOUSE_STATUS_MONITOR_ENABLE_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
    }
}
