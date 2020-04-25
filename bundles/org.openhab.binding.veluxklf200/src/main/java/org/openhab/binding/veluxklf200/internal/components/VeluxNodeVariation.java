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
 * Represents a node variation.
 *
 * @author emmanuel
 */
public enum VeluxNodeVariation {

    /** The not set. */
    NOT_SET((byte) 0),

    /** The tophung. */
    TOPHUNG((byte) 1),

    /** The kip. */
    KIP((byte) 2),

    /** The flat roof. */
    FLAT_ROOF((byte) 3),

    /** The sky light. */
    SKY_LIGHT((byte) 4),

    /** The unknown. */
    UNKNOWN((byte) -1);

    /** The variation code. */
    private byte variationCode;

    /**
     * Instantiates a new velux node variation.
     *
     * @param code
     *            the code
     */
    private VeluxNodeVariation(byte code) {
        this.variationCode = code;
    }

    /**
     * Gets the variation code.
     *
     * @return the variation code
     */
    public byte getVariationCode() {
        return this.variationCode;
    }

    /**
     * Creates the.
     *
     * @param c
     *            the c
     * @return the velux node variation
     */
    public static VeluxNodeVariation createFromCode(byte code) {
        switch (code) {
            case 0:
                return NOT_SET;
            case 1:
                return TOPHUNG;
            case 2:
                return KIP;
            case 3:
                return FLAT_ROOF;
            case 4:
                return SKY_LIGHT;
            default:
                return UNKNOWN;

        }
    }
}