package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum ProductGroup {
    /** Remote control. */
    ROLLER_SHUTTER((byte) 1, "Roller Shutter"), // This is an assumption, it is not documented
    /** Remote control. */
    WINDOW_OPENER((byte) 3, "Window Opener"), // This is an assumption, it is not documented
    /** Remote control. */
    REMOTE_CONTROL((byte) 14, "Remote control"),
    /** Unknown. */
    UNKNOWN((byte) 255, "Unknown");

    private static final Logger logger = LoggerFactory.getLogger(ProductGroup.class);
    private byte code;
    private String description;

    private ProductGroup(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static ProductGroup fromCode(byte code) {
        for (ProductGroup testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("No ProductGroup mapping for code: {}", code);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
