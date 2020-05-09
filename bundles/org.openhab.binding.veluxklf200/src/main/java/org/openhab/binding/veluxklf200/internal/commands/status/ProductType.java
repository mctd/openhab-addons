package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum ProductType {
    /** KMX Windows Opener */
    KMX((byte) 1, "KMX"),
    /** KLF200 gateway. */
    KLF200((byte) 3, "KLF200"),
    /** SSL roller shutter. */
    SSL((byte) 5, "SSL"),
    /** Unknown. */
    UNKNOWN((byte) 255, "??? Unknown ????");

    private static final Logger logger = LoggerFactory.getLogger(ProductType.class);
    private byte code;
    private String description;

    private ProductType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static ProductType fromCode(byte code) {
        for (ProductType testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
