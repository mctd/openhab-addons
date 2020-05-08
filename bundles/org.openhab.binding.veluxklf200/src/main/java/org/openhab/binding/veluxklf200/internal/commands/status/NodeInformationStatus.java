package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum NodeInformationStatus {
    /** OK - Request accepted. */
    OK((byte) 0, "OK - Request accepted"),
    /** Error – Request rejected. */
    ERROR_REJECTED((byte) 1, "Error – Request rejected"),
    /** Error – Invalid node index. */
    ERROR_INVALID_INDEX((byte) 2, "Error – Invalid node index"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private static final Logger logger = LoggerFactory.getLogger(NodeInformationStatus.class);
    private byte code;
    private String description;

    NodeInformationStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static NodeInformationStatus fromCode(byte code) {
        for (NodeInformationStatus testValue : values()) {
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
