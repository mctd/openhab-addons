package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_ALL_GROUPS_INFORMATION_REQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter used for {@link GW_GET_ALL_GROUPS_INFORMATION_REQ}
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public enum GroupType {
    /** The group type is a user group. */
    USER_GROUP((byte) 0, "User group"),
    /** The group type is a Room. */
    ROOM((byte) 1, "Room group"),
    /** The group type is a House. */
    HOUSE((byte) 2, "House group");

    private static final Logger logger = LoggerFactory.getLogger(GroupType.class);
    private byte code;
    private String description;

    GroupType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GroupType fromCode(byte code) {
        for (GroupType testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return USER_GROUP;
    }

    public byte getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
