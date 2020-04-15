/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.components;

/**
 * Used to indicate the velocity thata node is operating / configured for.
 *
 * @author MFK - Initial Contribution
 */
public enum VeluxVelocity {

    /** default. */
    DEFAULT((byte) 0, "Default"),

    /** silent. */
    SILENT((byte) 1, "Silent"),

    /** fast. */
    FAST((byte) 2, "Fast"),

    /** unknown. */
    UNKNOWN((byte) 254, "Unknown"),

    /** not available. */
    NOT_AVAILABLE((byte) 255, "Not Available");

    /** The velocity code. */
    private byte velocityCode;

    /** The display velocity. */
    private String displayVelocity;

    /**
     * Instantiates a new velux velocity.
     *
     * @param code
     *            the code
     * @param displayVelocity
     *            the display velocity
     */
    private VeluxVelocity(byte code, String displayVelocity) {
        this.velocityCode = code;
        this.displayVelocity = displayVelocity;
    }

    /**
     * Gets the velocity code.
     *
     * @return the velocity code
     */
    public byte getVelocityCode() {
        return this.velocityCode;
    }

    /**
     * Gets the display velocity.
     *
     * @return the display velocity
     */
    public String getDisplayVelocity() {
        return this.displayVelocity;
    }

    /**
     * Creates the from code.
     *
     * @param code
     *            the code
     * @return the velux velocity
     */
    public static VeluxVelocity createFromCode(byte code) {
        switch (code) {
            case 0:
                return DEFAULT;
            case 1:
                return SILENT;
            case 2:
                return FAST;
            case (byte) 254:
                return NOT_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }
}