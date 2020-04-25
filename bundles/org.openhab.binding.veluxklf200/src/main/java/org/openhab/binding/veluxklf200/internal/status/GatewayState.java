package org.openhab.binding.veluxklf200.internal.status;

public enum GatewayState {
    /** Test mode. */
    TEST_MODE((byte) 0, "Test mode"),
    /** Gateway mode, no actuator nodes in the system table. */
    GATEWAY_NO_ACTUATOR((byte) 1, "Gateway mode, no actuator nodes in the system table"),
    /** Gateway mode, with one or more actuator nodes in the system table. */
    GATEWAY((byte) 2, "Gateway mode, with one or more actuator nodes in the system table"),
    /** Beacon mode, not configured by a remote controller. */
    BEACON_NOT_CONFIGURED((byte) 3, "Beacon mode, not configured by a remote controller"),
    /** Beacon mode, has been configured by a remote controller. */
    BEACON((byte) 4, "Beacon mode, has been configured by a remote controller"),
    /** Reserved. */
    RESERVED((byte) 5, "Reserved"); // 5 -> 255

    private byte code;
    private String description;

    GatewayState(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static GatewayState fromCode(byte code) {
        for (GatewayState testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        // Reserved values are 5 to 255
        return RESERVED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
