package org.openhab.binding.veluxklf200.internal.status;

public enum PowerMode {
    /** Always alive. */
    ALWAYS_ALIVE((byte) 0, "Always alive"),
    /** Low power mode. */
    LOW_POWER_MODE((byte) 1, "Low power mode");

    private byte code;
    private String description;

    PowerMode(byte code, String description) {
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

        return null; // TODO: throw exception ?
    }

    @Override
    public String toString() {
        return this.description;
    }
}
