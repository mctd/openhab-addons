package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum GetAllGroupsStatus {
    /** OK – Request accepted. */
    OK((byte) 0, "OK – Request accepted"),
    /** Error – Request failed. */
    ERROR_FAILED((byte) 1, "Error – Request failed"),
    /** Error – No groups available. */
    ERROR_NO_GROUPS((byte) 2, "Error – No groups available"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private static final Logger logger = LoggerFactory.getLogger(GetAllGroupsStatus.class);
    private byte code;
    private String description;

    private GetAllGroupsStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static GetAllGroupsStatus fromCode(byte code) {
        for (GetAllGroupsStatus testValue : values()) {
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
