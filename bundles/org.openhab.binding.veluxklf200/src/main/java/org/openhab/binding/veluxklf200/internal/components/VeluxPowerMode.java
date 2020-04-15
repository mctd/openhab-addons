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
 * Indicates the power mode of the velux node. Typically if it is a mains
 * operated node, the power mode with be ALWAYS_ACTIVE, but for battery or
 * solar, LOW_POWER.
 *
 * @author MFK - Initial Contribution
 */
public enum VeluxPowerMode {

    /** The always alive. */
    ALWAYS_ALIVE((byte) 0),

    /** The low power. */
    LOW_POWER((byte) 1),

    /** The unknown. */
    UNKNOWN((byte) -1);

    /** The power code. */
    private byte powerCode;

    /**
     * Instantiates a new velux power mode.
     *
     * @param code
     *            the code
     */
    private VeluxPowerMode(byte code) {
        this.powerCode = code;
    }

    /**
     * Gets the power code.
     *
     * @return the power code
     */
    public byte getPowerCode() {
        return this.powerCode;
    }

    /**
     * Creates the Power Mode from code.
     *
     * @param code
     *            the code
     * @return the velux power mode
     */
    public static VeluxPowerMode createFromCode(byte code) {
        switch (code) {
            case 0:
                return ALWAYS_ALIVE;
            case 1:
                return LOW_POWER;
            default:
                return UNKNOWN;
        }
    }
}