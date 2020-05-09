package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GetSceneInformationStatus;

@NonNullByDefault
public class GW_GET_SCENE_INFORMATION_CFM extends BaseConfirmationResponse {
    private GetSceneInformationStatus status;
    private int sceneId;

    public GW_GET_SCENE_INFORMATION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = GetSceneInformationStatus.fromCode(this.getCommandFrame().readByte(1));
        this.sceneId = this.getCommandFrame().readByteAsInt(2);
    }

    public GetSceneInformationStatus getStatus() {
        return this.status;
    }

    public int getSceneId() {
        return this.sceneId;
    }
}
