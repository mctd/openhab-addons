package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum Status {
    /** The status is from a user activation. */
    STATUS_USER((byte) 0x01, "The status is from a user activation"),
    /** The status is from a rain sensor activation. */
    STATUS_RAIN((byte) 0x02, "The status is from a rain sensor activation"),
    /** The status is from a timer generated action. */
    STATUS_TIMER((byte) 0x03, "The status is from a timer generated action"),
    /** The status is from a UPS generated action. */
    STATUS_UPS((byte) 0x05, "The status is from a UPS generated action"),
    /** The status is from an automatic program generated action. */
    STATUS_PROGRAM((byte) 0x08, "The status is from an automatic program generated action"),
    /** The status is from a Wind sensor generated action. */
    STATUS_WIND((byte) 0x09, "The status is from a Wind sensor generated action"),
    /** The status is from an actuator generated action. */
    STATUS_MYSELF((byte) 0x0A, "The status is from an actuator generated action"),
    /** The status is from a automatic cycle generated action. */
    STATUS_AUTOMATIC_CYCLE((byte) 0x0B, "The status is from a automatic cycle generated action"),
    /** The status is from an emergency or a security generated action. */
    STATUS_EMERGENCY((byte) 0x0C, "The status is from an emergency or a security generated action"),
    /** The status is from an unknown command originator action. */
    STATUS_UNKNOWN((byte) 0xFF, "The status is from an unknown command originator action");

    private static final Logger logger = LoggerFactory.getLogger(Status.class);
    private byte code;
    private String description;

    private Status(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static Status fromCode(byte code) {
        for (Status testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return STATUS_UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
