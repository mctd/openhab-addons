package org.openhab.binding.veluxklf200.internal.status;

public enum ProductType {
    /** SSL roller shutter. */
    SSL((byte) 5, "SSL Solar Roller Shutter"),
    /** Low power mode. */
    UNKNOWN((byte) 255, "??? Unknown ????");

    private byte code;
    private String description;

    ProductType(byte code, String description) {
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

        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
