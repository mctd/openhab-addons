package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum PowerMode {
    /** Always alive. */
    ALWAYS_ALIVE((byte) 0, "Always alive"),
    /** Low power mode. */
    LOW_POWER_MODE((byte) 1, "Low power mode");

    private static final Logger logger = LoggerFactory.getLogger(PowerMode.class);
    private byte code;
    private String description;

    private PowerMode(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static PowerMode fromCode(byte code) {
        for (PowerMode testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return ALWAYS_ALIVE;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
