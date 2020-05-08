package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.events.BridgeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GW_SESSION_FINISHED_NTF sent when the session started by GW_COMMAND_SEND_REQ, is over.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_SESSION_FINISHED_NTF extends BaseNotificationResponse implements BridgeEvent {
    private static final Logger logger = LoggerFactory.getLogger(GW_SESSION_FINISHED_NTF.class);

    private short sessionID;

    public GW_SESSION_FINISHED_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.sessionID = this.getCommandFrame().readShort(1);

        logger.debug("GW_SESSION_FINISHED_NTF: Command with sessionID {} is over.", this.sessionID);
    }
}
