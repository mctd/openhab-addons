package org.openhab.binding.veluxklf200.internal.status;

/**
 * List of actuator types
 *
 * @author emmanuel
 *
 */
public enum NodeType {
    INTERIOR_VENITIAN_BLIND("Interior Venetian Blind"),
    ROLLER_SHUTTER("Roller Shutter"),
    EXTERIOR_AWNING("Vertical Exterior Awning"),
    WINDOW_OPENER("Window opener"),
    GARAGE_OPENER("Garage door opener"),
    LIGHT("Light"),
    GATE_OPENER("Gate opener"),
    DOOR_LOCK("Door lock"),
    WINDOW_LOCK("Window lock"),
    VERICAL_INTERIOR_BLINDS("Vertical Interior Blinds"),
    DUAL_ROLLER_SHUTTER("Dual Roller Shutter"),
    ON_OFF_SWITCH("On/Off switch"),
    HORIZONTAL_AWNING("Horizontal awning"),
    EXTERIOR_VENITIAN_BLIND("Exterior Venetian blind"),
    LOUVER_BLIND("Louver blind"),
    CURTAIN_TRACK("Curtain track"),
    VENTILATION_POINT("Ventilation point"),
    EXTERIOR_HEATING("Exterior heating"),
    SWINGING_SHUTTERS("Swinging Shutters");

    private String description;

    private NodeType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
