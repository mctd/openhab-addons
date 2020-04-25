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
 * Helper class to help translate the position values of a Velux node into more
 * readable / usable references. KLF returns position from 0x0000 (OPEN) to 0xC800 (CLOSED).
 *
 * @author emmanuel
 */
public class VeluxPosition {

    /** The Constant POSITION_OPEN. */
    private static final short POSITION_OPEN = (short) 0x0000;

    /** The Constant PCT_POSITION_INC. */
    private static final short PCT_POSITION_INC = (short) 0x200;

    /** The Constant POSITION_UNKNOWN. */
    private static final short POSITION_UNKNOWN = (short) 0xF7FF;

    /** The position (percent closed), null if unknown. */
    private Integer position;

    /**
     * Instantiates a new Velux position.
     *
     * @param position
     *            the position (expressed in percent closed)
     */
    public VeluxPosition(Integer position) {
        if (position == null) {
            this.position = position;
        } else if (position < 0) {
            this.position = 0;
        } else if (position > 100) {
            this.position = 100;
        } else {
            this.position = position;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.position == null) {
            return "Unknown";
        }
        return this.position + "% closed";
    }

    /**
     * Gets the percentage closed.
     *
     * @return the percentage closed, null if unknown
     */
    public Integer getPosition() {
        return this.position;
    }

    public short toKLFCode() {
        if (this.position == null) {
            return POSITION_UNKNOWN;
        } else {
            return (short) (POSITION_OPEN + ((short) (this.position * PCT_POSITION_INC)));
        }
    }

    /**
     * Creates the Position representation from KLF code.
     *
     * @param b1
     *            position byte 1
     * @param b2
     *            position byte 2
     * @return the velux position
     */
    public static VeluxPosition createFromCode(short klfPosition) {
        Integer position = (klfPosition == POSITION_UNKNOWN) ? null : (klfPosition & 0xffff) / PCT_POSITION_INC;
        return new VeluxPosition(position);
    }
}