/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.status;

/**
 * Represents a Velux Velocity setting.
 *
 * @author emmanuel
 */
public enum Velocity {

    /** Default. */
    DEFAULT((byte) 0, "Default"),

    /** Silent. */
    SILENT((byte) 1, "Silent"),

    /** Fast. */
    FAST((byte) 2, "Fast"),

    /** Unknown. */
    UNKNOWN((byte) 254, "Unknown"),

    /** Not available. */
    NOT_AVAILABLE((byte) 255, "Not Available");

    private byte code;
    private String displayName;

    /**
     * Instantiates a new Velux velocity.
     *
     * @param code
     *            the code
     * @param displayName
     *            the display name of velocity
     */
    private Velocity(byte code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Gets the velocity code.
     *
     * @return the velocity code
     */
    public byte getCode() {
        return this.code;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Creates the from code.
     *
     * @param code Code representation of the velocity.
     * @return Matching velocity
     */
    public static Velocity fromCode(byte code) {
        for (Velocity testVal : values()) {
            if (testVal.getCode() == code) {
                return testVal;
            }
        }

        // TODO : log warning unmapped value
        return UNKNOWN;
    }
}