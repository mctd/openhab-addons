package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum GetAllNodesStatus {
    /** OK – Request accepted. */
    OK((byte) 0, "OK – Request accepted"),
    /** Error – System table empty. */
    ERROR_NO_NODES((byte) 1, "Error – System table empty"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private static final Logger logger = LoggerFactory.getLogger(GetAllNodesStatus.class);
    private byte code;
    private String description;

    GetAllNodesStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static GetAllNodesStatus fromCode(byte code) {
        for (GetAllNodesStatus testValue : values()) {
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
