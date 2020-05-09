package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum GetSceneInformationStatus {
    /** OK - Request accepted. */
    OK((byte) 0, "OK - Request accepted"),
    /** Error – Invalid node index. */
    ERROR_INVALID_INDEX((byte) 1, "Error – Invalid scene index"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private static final Logger logger = LoggerFactory.getLogger(GetSceneInformationStatus.class);
    private byte code;
    private String description;

    private GetSceneInformationStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static GetSceneInformationStatus fromCode(byte code) {
        for (GetSceneInformationStatus testValue : values()) {
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
