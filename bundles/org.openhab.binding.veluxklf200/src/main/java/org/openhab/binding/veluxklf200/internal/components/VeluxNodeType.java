/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.components;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.openhab.binding.veluxklf200.internal.VeluxKLF200BindingConstants;

/**
 * Represents a type of Velux node.
 *
 * @author emmanuel
 */
public enum VeluxNodeType {

    /** The interior venetian blind. */
    INTERIOR_VENETIAN_BLIND("Interior Venetian Blind", (byte) 0, "", VeluxKLF200BindingConstants.THING_TYPE_ACTUATOR),

    /** The roller shutter. */
    ROLLER_SHUTTER("Roller Shutter", (byte) 0, "", VeluxKLF200BindingConstants.THING_TYPE_ACTUATOR),

    /** The vertical exterior awning. */
    VERTICAL_EXTERIOR_AWNING("Vertical Exterior Awning", (byte) 0, "", null),

    /** The window opener. */
    WINDOW_OPENER("Window Opener", (byte) 0, "", null),

    /** The garage door opener. */
    GARAGE_DOOR_OPENER("Garage Door Opener", (byte) 0, "", null),

    /** The light. */
    LIGHT("Light", (byte) 0, "", null),

    /** The gate opener. */
    GATE_OPENER("Gate Opener", (byte) 0, "", null),

    /** The door lock. */
    DOOR_LOCK("Door Lock", (byte) 0, "", null),

    /** The window lock. */
    WINDOW_LOCK("Window Lock", (byte) 0, "", null),

    /** The vertical interior blinds. */
    VERTICAL_INTERIOR_BLINDS("Vertical Interior Blinds", (byte) 0, "", null),

    /** The dual roller shutter. */
    DUAL_ROLLER_SHUTTER("Dual Roller Shutter", (byte) 0, "", null),

    /** The on off switch. */
    ON_OFF_SWITCH("On/Off Switch", (byte) 0, "", null),

    /** The horizontal awning. */
    HORIZONTAL_AWNING("Horizontal Awning", (byte) 0, "", null),

    /** The exterior venetian blind. */
    EXTERIOR_VENETIAN_BLIND("Exterior Venetian Blind", (byte) 0, "", null),

    /** The louver blind. */
    LOUVER_BLIND("Louver Blind", (byte) 0, "", null),

    /** The curtain track. */
    CURTAIN_TRACK("Curtain Track", (byte) 0, "", null),

    /** The ventilation point. */
    VENTILATION_POINT("Ventilation Point", (byte) 0, "", null),

    /** The exterior heating. */
    EXTERIOR_HEATING("Exterior Heating", (byte) 0, "", null),

    /** The swinging shutters. */
    SWINGING_SHUTTERS("Swinging Shutters", (byte) 0, "", null),

    /** The unknown. */
    UNKNOWN("Unknown Device", (byte) 0, "", null);

    /** The display name. */
    private String displayName;

    /** The variation description. */
    private String variationDescription;

    /** The variation code. */
    private byte variationCode;

    /** The thing type UID **/
    private ThingTypeUID thingTypeUID;

    /**
     * Instantiates a new velux node type.
     *
     * @param displayName
     *            the display name
     * @param variationCode
     *            the variation code
     * @param variationDescription
     *            the variation description
     * @param thingTypeUID
     *            the matching thing type UID
     */
    private VeluxNodeType(String displayName, byte variationCode, String variationDescription,
            ThingTypeUID thingTypeUID) {
        this.displayName = displayName;
        this.variationCode = variationCode;
        this.variationDescription = variationDescription;
        this.thingTypeUID = thingTypeUID;
    }

    /**
     * Sets the variation code.
     *
     * @param code
     *            the code
     * @return the velux node type
     */
    private VeluxNodeType setVariationCode(byte code) {
        this.variationCode = code;
        return this;
    }

    /**
     * Sets the variation desc.
     *
     * @param desc
     *            the desc
     * @return the velux node type
     */
    private VeluxNodeType setVariationDesc(String desc) {
        this.variationDescription = desc;
        return this;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the variation description.
     *
     * @return the variation description
     */
    public String getVariationDescription() {
        return variationDescription;
    }

    /**
     * Gets the variation code.
     *
     * @return the variation code
     */
    public byte getVariationCode() {
        return variationCode;
    }

    /**
     * Gets the matching ThingTypeUID
     *
     * @return the matching ThingTypeUID
     */
    public ThingTypeUID getThingTypeUID() {
        return this.thingTypeUID;
    }

    /**
     * Creates the from code.
     *
     * @param code
     *            the code
     * @return the velux node type
     */
    public static VeluxNodeType createFromCode(short code) {
        switch (code) {
            case 0x0040:
                return INTERIOR_VENETIAN_BLIND;
            case 0x0080:
                return ROLLER_SHUTTER;
            case 0x0081:
                return ROLLER_SHUTTER.setVariationCode((byte) 1).setVariationDesc("Adjustable Slats Rolling Shutter");
            case 0x0082:
                return ROLLER_SHUTTER.setVariationCode((byte) 2).setVariationDesc("With Projection");
            case 0x00C0:
                return VERTICAL_EXTERIOR_AWNING;
            case 0x0100:
                return WINDOW_OPENER.setVariationCode((byte) 0);
            case 0x0101:
                return WINDOW_OPENER.setVariationCode((byte) 1).setVariationDesc("With Integrated Rain Sensor");
            case 0x0140:
                return GARAGE_DOOR_OPENER;
            case 0x017A:
                return GARAGE_DOOR_OPENER;
            case 0x0180:
                return LIGHT;
            case 0x01BA:
                return LIGHT.setVariationCode((byte) 1).setVariationDesc("Only Supporting On/Off");
            case 0x01C0:
                return GATE_OPENER;
            case 0x01FA:
                return GATE_OPENER;
            case 0x0240:
                return DOOR_LOCK;
            case 0x0241:
                return WINDOW_LOCK;
            case 0x0280:
                return VERTICAL_INTERIOR_BLINDS;
            case 0x0340:
                return DUAL_ROLLER_SHUTTER;
            case 0x03C0:
                return ON_OFF_SWITCH;
            case 0x0400:
                return HORIZONTAL_AWNING;
            case 0x0440:
                return EXTERIOR_VENETIAN_BLIND;
            case 0x0480:
                return LOUVER_BLIND;
            case 0x04C0:
                return CURTAIN_TRACK;
            case 0x0500:
                return VENTILATION_POINT;
            case 0x0501:
                return VENTILATION_POINT.setVariationCode((byte) 1).setVariationDesc("Air Inlet");
            case 0x0502:
                return VENTILATION_POINT.setVariationCode((byte) 2).setVariationDesc("Air Transfer");
            case 0x0503:
                return VENTILATION_POINT.setVariationCode((byte) 3).setVariationDesc("Air Outlet");
            case 0x0540:
                return EXTERIOR_HEATING;
            case 0x057A:
                return EXTERIOR_HEATING;
            case 0x0600:
                return SWINGING_SHUTTERS;
            case 0x0601:
                return SWINGING_SHUTTERS.setVariationCode((byte) 1)
                        .setVariationDesc("With Independent Handling of the Leaves");
            default:
                return UNKNOWN;
        }
    }

}