package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_SCENE_LIST_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_SCENE_LIST_CFM.class);

    private int totalNumberOfScenes;

    public GW_GET_SCENE_LIST_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.totalNumberOfScenes = this.getCommandFrame().readByteAsInt(1);

        logger.debug("Get Scene List result: total number of scenes: {}", this.totalNumberOfScenes);
    }

    public int getTotalNumberOfScenes() {
        return totalNumberOfScenes;
    }
}
