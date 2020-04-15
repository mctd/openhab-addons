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
 * Indicates the interim state of a velux command.
 *
 * @author MFK - Initial Contribution
 */
public enum VeluxState {

    /** The non executing. */
    NON_EXECUTING((byte) 0),

    /** The error executing. */
    ERROR_EXECUTING((byte) 1),

    /** The not used. */
    NOT_USED((byte) 2),

    /** The awaiting power. */
    AWAITING_POWER((byte) 3),

    /** The executing. */
    EXECUTING((byte) 4),

    /** The done. */
    DONE((byte) 5),

    /** The unknown. */
    UNKNOWN((byte) 255);

    /** The state code. */
    private byte stateCode;

    /**
     * Instantiates a new velux state.
     *
     * @param code
     *            the code
     */
    private VeluxState(byte code) {
        this.stateCode = code;
    }

    /**
     * Gets the state code.
     *
     * @return the state code
     */
    public byte getStateCode() {
        return this.stateCode;
    }

    /**
     * Creates the.
     *
     * @param code
     *            the code
     * @return the velux state
     */
    public static VeluxState createFromCode(byte code) {
        switch (code) {
            case 0:
                return NON_EXECUTING;
            case 1:
                return ERROR_EXECUTING;
            case 2:
                return NOT_USED;
            case 3:
                return AWAITING_POWER;
            case 4:
                return EXECUTING;
            case 5:
                return DONE;
            default:
                return UNKNOWN;
        }
    }
}