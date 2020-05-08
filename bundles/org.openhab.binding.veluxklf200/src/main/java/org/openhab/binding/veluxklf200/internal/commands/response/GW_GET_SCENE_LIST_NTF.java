package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.events.BridgeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This command tells how long it takes until the actuator has reached the desired position.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_SCENE_LIST_NTF extends BaseNotificationResponse implements BridgeEvent {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_SCENE_LIST_NTF.class);

    private int numberOfObject;
    private SceneDescription[] scenes;
    private int remainingNumberOfObject;

    public class SceneDescription {
        private int id;
        private String name;

        public SceneDescription(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return String.format("%d: %s", this.getId(), this.getName());
        }
    }

    public GW_GET_SCENE_LIST_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.numberOfObject = this.getCommandFrame().readByte(1) & 0xFF;

        this.scenes = new SceneDescription[this.numberOfObject];
        // Each scene is stored in array as 1 + 64 bytes
        int i = 0;
        for (; i < this.numberOfObject; i++) {
            // Id is the first byte
            int sceneId = this.getCommandFrame().readByteAsInt(2 + (65 * i));
            // Name is stored on 64 bytes
            String sceneName = this.getCommandFrame().readString(3 + (65 * i), 64);

            this.scenes[i] = new SceneDescription(sceneId, sceneName);
        }

        this.remainingNumberOfObject = this.getCommandFrame().readByteAsInt(2 + (65 * i));

        logger.debug("numberOfObject: {}, scenes: {}, remainingNumberOfObject: {}.", this.numberOfObject, this.scenes,
                this.remainingNumberOfObject);
    }

    public SceneDescription[] getScenes() {
        return this.scenes;
    }

    public int getRemainingNumberOfObject() {
        return this.remainingNumberOfObject;
    }
}
