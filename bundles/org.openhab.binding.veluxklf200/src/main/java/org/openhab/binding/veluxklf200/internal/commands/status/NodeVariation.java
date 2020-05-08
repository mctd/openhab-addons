package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum NodeVariation {
    /** Not set. */
    NOT_SET((byte) 0, "Not set"),
    /** Window is a top hung window. */
    TOPHUNG((byte) 1, "Window is a top hung window"),
    /** Window is a kip window. */
    KIP((byte) 2, "Window is a kip window"),
    /** Window is a flat roof. */
    FLAT_ROOF((byte) 3, "Window is a flat roof"),
    /** Window is a sky light. */
    SKY_LIGHT((byte) 4, "Window is a sky light");

    private static final Logger logger = LoggerFactory.getLogger(NodeVariation.class);
    private byte code;
    private String description;

    NodeVariation(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static NodeVariation fromCode(byte code) {
        for (NodeVariation testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return NOT_SET;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
