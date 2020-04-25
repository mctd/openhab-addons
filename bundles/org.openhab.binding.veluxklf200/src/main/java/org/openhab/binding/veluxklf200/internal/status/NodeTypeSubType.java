package org.openhab.binding.veluxklf200.internal.status;

/**
 * List of actuator subtypes and their use of Main Parameter and Functional Parameters
 *
 * @author emmanuel
 *
 */
public enum NodeTypeSubType {
    /** Interior Venetian Blind. */
    INTERIOR_VENITIAN_BLIND((short) 0x0040, "", NodeType.INTERIOR_VENITIAN_BLIND),
    /** Roller Shutter. */
    ROLLER_SHUTTER((short) 0x0080, "", NodeType.ROLLER_SHUTTER),
    /** Adjustable slats rolling shutter. */
    ADJUSTABLE_SLATS_ROLLING_SHUTTER((short) 0x0081, "Adjustable slats rolling shutter", NodeType.ROLLER_SHUTTER),
    /** Roller shutter with projection. */
    ROLLER_SHUTTER_WITH_PROJECTION((short) 0x0082, "With projection", NodeType.ROLLER_SHUTTER);

    // 0x0040 Interior Venetian Blind
    // 0x0080 Roller Shutter
    // 0x0081 Adjustable slats rolling shutter
    // 0x0082 With projection
    // 0x00C0 Vertical Exterior Awning
    // 0x0100 Window opener
    // 0x0101 Window opener with integrated rain sensor
    // 0x0140 Garage door opener
    // 0x017A
    // 0x0180 Light Light intensity
    // 0x01BA Light only supporting on/off
    // 0x01C0 Gate opener
    // 0x01FA
    // 0x0240 Door lock
    // 0x0241 Window lock
    // 0x0280 Vertical Interior Blinds
    // 0x0340 Dual Roller Shutter
    // 0x03C0 On/Off switch
    // 0x0400 Horizontal awning
    // 0x0440 Exterior Venetian blind
    // 0x0480 Louver blind
    // 0x04C0 Curtain track
    // 0x0500 Ventilation point
    // 0x0501 Air inlet
    // 0x0502 Air transfer
    // 0x0503 Air outlet
    // 0x0540 Exterior heating
    // 0x57A
    // 0x0600 Swinging Shutters
    // 0x0601 Swinging Shutter with independent handling of the leaves

    private short code;
    private String description;
    private NodeType nodeType;

    NodeTypeSubType(short code, String description, NodeType nodeType) {
        this.code = code;
        this.description = description;
        this.nodeType = nodeType;
    }

    public short getCode() {
        return this.code;
    }

    public static NodeTypeSubType fromCode(short code) {
        for (NodeTypeSubType testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        return null; // TODO: throw exception ?
    }

    @Override
    public String toString() {
        return this.nodeType.toString() + " " + this.description;
    }
}
