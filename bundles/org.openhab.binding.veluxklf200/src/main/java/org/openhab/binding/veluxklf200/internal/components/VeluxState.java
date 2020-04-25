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
 * Indicates the state of a Velux command.
 *
 * @author emmanuel
 */
public enum VeluxState {

    /** Non executing. */
    NON_EXECUTING((byte) 0),

    /** Error executing. */
    ERROR_EXECUTING((byte) 1),

    /** Not used. */
    NOT_USED((byte) 2),

    /** Awaiting power. */
    AWAITING_POWER((byte) 3),

    /** Executing. */
    EXECUTING((byte) 4),

    /** Done. */
    DONE((byte) 5),

    /** Unknown. */
    UNKNOWN((byte) 255);

    /** The state code. */
    private byte stateCode;

    /**
     * Instantiates a new Velux state.
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
     * Creates a state from its code.
     *
     * @param code
     *            the code
     * @return the Velux state
     */
    public static VeluxState createFromCode(byte code) {
        for (VeluxState testState : VeluxState.values()) {
            if (testState.getStateCode() == code) {
                return testState;
            }
        }
        return UNKNOWN;
    }
}