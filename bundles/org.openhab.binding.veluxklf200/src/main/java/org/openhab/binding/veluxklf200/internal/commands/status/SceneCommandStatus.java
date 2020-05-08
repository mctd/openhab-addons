package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum SceneCommandStatus {
    /** OK - Request accepted. */
    OK((byte) 0, "OK - Request accepted"),
    /** Error – Request rejected. */
    ERROR_INVALID_PARAMETER((byte) 1, "Error – Invalid parameter"),
    /** Error – Invalid node index. */
    ERROR_REQUEST_REJECTED((byte) 2, "Error – Request rejected"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private static final Logger logger = LoggerFactory.getLogger(SceneCommandStatus.class);
    private byte code;
    private String description;

    SceneCommandStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static SceneCommandStatus fromCode(byte code) {
        for (SceneCommandStatus testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        // Reserved values are 5 to 255
        return RESERVED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
