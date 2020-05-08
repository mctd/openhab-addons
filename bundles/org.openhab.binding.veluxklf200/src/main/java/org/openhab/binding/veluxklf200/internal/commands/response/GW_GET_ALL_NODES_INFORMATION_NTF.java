package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.events.BridgeEvent;
import org.openhab.binding.veluxklf200.internal.events.NodePositionEvent;

/**
 * Acknowledge to GW_GET_ALL_NODES_INFORMATION_REQ.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_ALL_NODES_INFORMATION_NTF extends GW_GET_NODE_INFORMATION_NTF
        implements NodePositionEvent, BridgeEvent {
    public GW_GET_ALL_NODES_INFORMATION_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
    }
}
