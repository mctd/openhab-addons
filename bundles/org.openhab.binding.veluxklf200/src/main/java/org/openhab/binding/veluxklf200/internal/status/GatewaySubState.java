package org.openhab.binding.veluxklf200.internal.status;

public enum GatewaySubState {
    /** Idle state. */
    IDLE((byte) 0x00, "Idle state"),
    /** Performing task in Configuration Service handler */
    PERFORMING_TASK_CONFIG((byte) 0x01, "Performing task in Configuration Service handler"),
    /** Performing Scene Configuration */
    PERFORMING_SCENE_CONFIG((byte) 0x02, "Performing Scene Configuration"),
    /** Performing Information Service Configuration. */
    PERFORMING_INFORMATION_SERVICE_CONFIG((byte) 0x03, "Performing Information Service Configuration"),
    /** Performing Contact input Configuration. */
    PERFORMING_CONTACT_INPUT_CONFIG((byte) 0x04, "Performing Contact input Configuration"),
    /** Performing task in Command Handler */
    PERFORMING_TASK_COMMAND((byte) 0x80, "Performing task in Command Handler"),
    /** Performing task in Activate Group Handler */
    PERFORMING_TASK_ACTIVATE_GROUP((byte) 0x81, "Performing task in Activate Group Handler"),
    /** Performing task in Activate Scene Handler */
    PERFORMING_TASK_ACTIVATE_SCENE((byte) 0x82, "Performing task in Activate Scene Handler"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private byte code;
    private String description;

    GatewaySubState(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static GatewaySubState fromCode(byte code) {
        for (GatewaySubState testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        // Reserved values are all other values.
        return RESERVED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
