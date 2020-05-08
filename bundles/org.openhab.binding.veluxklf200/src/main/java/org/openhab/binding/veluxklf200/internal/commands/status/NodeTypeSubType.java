package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * List of actuator subtypes and their use of Main Parameter and Functional Parameters
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public enum NodeTypeSubType {
    /** Unknown Type */
    UNKNOWN((short) 0x0, "Unknown type", NodeType.UNKNOWN),
    /** Interior Venetian Blind. */
    INTERIOR_VENITIAN_BLIND((short) 0x0040, "Interior Venetian Blind", NodeType.INTERIOR_VENITIAN_BLIND),
    /** Roller Shutter. */
    ROLLER_SHUTTER((short) 0x0080, "Roller Shutter", NodeType.ROLLER_SHUTTER),
    /** Adjustable slats rolling shutter. */
    ADJUSTABLE_SLATS_ROLLING_SHUTTER((short) 0x0081, "Adjustable slats rolling shutter", NodeType.ROLLER_SHUTTER),
    /** Roller shutter with projection. */
    ROLLER_SHUTTER_WITH_PROJECTION((short) 0x0082, "Roller Shutter with projection", NodeType.ROLLER_SHUTTER),
    /** Vertical Exterior Awning. */
    VERTICAL_EXTERIOR_AWNING((short) 0x00C0, "Vertical Exterior Awning", NodeType.EXTERIOR_AWNING),
    /** Window opener. */
    WINDOW_OPENER((short) 0x0100, "Window opener", NodeType.WINDOW_OPENER),
    /** Window opener. */
    WINDOW_OPENER_WITH_RAIN_SENSOR((short) 0x0101, "Window opener with integrated rain sensor", NodeType.WINDOW_OPENER),
    /** Garage door opener. */
    GARAGE_DOOR_OPENER((short) 0x0140, "Garage door opener", NodeType.GARAGE_DOOR_OPENER),
    /** Garage door opener. */
    GARAGE_DOOR_OPENER2((short) 0x017A, "Garage door opener", NodeType.GARAGE_DOOR_OPENER),
    /** Light. */
    LIGHT((short) 0x0180, "Light", NodeType.LIGHT),
    /** ON/OFF Light. */
    LIGHT_ON_OFF((short) 0x01BA, "Light only supporting on/off", NodeType.LIGHT),
    /** Gate opener. */
    GATE_OPENER((short) 0x01C0, "Gate opener", NodeType.GATE_OPENER),
    /** Gate opener. */
    GATE_OPENER2((short) 0x01FA, "Gate opener", NodeType.GATE_OPENER),
    /** Door lock. */
    DOOR_LOCK((short) 0x0240, "Door lock", NodeType.DOOR_LOCK),
    /** Window lock. */
    WINDOW_LOCK((short) 0x0241, "Window lock", NodeType.WINDOW_LOCK),
    /** Vertical Interior Blinds */
    VERTICAL_INTERIOR_BLINDS((short) 0x0280, "Vertical Interior Blinds", NodeType.VERTICAL_INTERIOR_BLINDS),
    /** Dual roller shutter */
    DUAL_ROLLER_SHUTTER((short) 0x0340, "Dual roller shutter", NodeType.DUAL_ROLLER_SHUTTER),
    /** On/Off switch. */
    ON_OFF_SWITCH((short) 0x03C0, "On/Off switch", NodeType.ON_OFF_SWITCH),
    /** Horizontal awning. */
    HORIZONTAL_AWNING((short) 0x0400, "Horizontal awning", NodeType.HORIZONTAL_AWNING),
    /** Exterior Venetian blind. */
    EXTERIOR_VENITIAN_BLIND((short) 0x0440, "", NodeType.EXTERIOR_VENITIAN_BLIND),
    /** Louver blind. */
    LOUVER_BLIND((short) 0x0480, "Louver blind", NodeType.LOUVER_BLIND),
    /** Curtain track. */
    CURTAIN_TRACK((short) 0x04C0, "Curtain track", NodeType.CURTAIN_TRACK),
    /** Ventilation point. */
    VENTILATION_POINT((short) 0x0500, "Ventilation point", NodeType.VENTILATION_POINT),
    /** Ventilation point air inlet. */
    VENTILATION_POINT_AIR_INLET((short) 0x0501, "Ventilation point air inlet", NodeType.VENTILATION_POINT),
    /** Ventilation point air transfer. */
    VENTILATION_POINT_AIR_TRANSFER((short) 0x0502, "Ventilation point air transfer", NodeType.VENTILATION_POINT),
    /** Ventilation point. */
    VENTILATION_POINT_AIR_OUTLET((short) 0x0503, "Ventilation point air outlet", NodeType.VENTILATION_POINT),
    /** Exterior heating. */
    EXTERIOR_HEATING((short) 0x0540, "Exterior heating", NodeType.EXTERIOR_HEATING),
    /** Exterior heating. */
    EXTERIOR_HEATING2((short) 0x57A, "Exterior heating", NodeType.EXTERIOR_HEATING),
    /** Swinging Shutters. */
    SWINGING_SHUTTERS((short) 0x0600, "Swinging Shutters", NodeType.SWINGING_SHUTTERS),
    /** Swinging Shutter with independent handling of the leaves. */
    SWINGING_SHUTTERS_WITH_LEAVES((short) 0x0601, "Swinging Shutter with independent handling of the leaves",
            NodeType.SWINGING_SHUTTERS);

    private static final Logger logger = LoggerFactory.getLogger(NodeTypeSubType.class);
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

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public static NodeTypeSubType fromCode(short code) {
        for (NodeTypeSubType testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.nodeType.toString(), this.description);
    }
}
