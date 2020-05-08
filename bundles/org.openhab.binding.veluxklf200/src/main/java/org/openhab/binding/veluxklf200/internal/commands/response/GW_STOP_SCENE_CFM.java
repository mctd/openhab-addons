package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.SceneCommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_STOP_SCENE_CFM extends BaseConfirmationResponse {
    private final Logger logger = LoggerFactory.getLogger(GW_ACTIVATE_SCENE_CFM.class);

    private SceneCommandStatus status;
    private int sessionId;

    public GW_STOP_SCENE_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = SceneCommandStatus.fromCode(this.getCommandFrame().readByte(1));
        this.sessionId = this.getCommandFrame().readShortAsInt(2);

        logger.debug("Status: {}, SessionId: {}", this.getStatus(), this.getSessionId());
    }

    public SceneCommandStatus getStatus() {
        return this.status;
    }

    public int getSessionId() {
        return this.sessionId;
    }
}
