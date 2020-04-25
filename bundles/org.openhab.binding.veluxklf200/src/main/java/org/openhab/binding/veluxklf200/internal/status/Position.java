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
 * Translates KLF position values to human readable values.
 *
 * @author emmanuel
 */
public class Position {
    private static final short POSITION_OPEN = (short) 0x0000;
    private static final short PCT_POSITION_INC = (short) 0x200;
    private static final short POSITION_UNKNOWN = (short) 0xF7FF;
    private Integer position;

    /**
     * Instantiates a new Velux position.
     *
     * @param position
     *            the position (expressed in percent closed)
     */
    public Position(Integer position) {
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

    public short toCode() {
        if (this.position == null) {
            return POSITION_UNKNOWN;
        } else {
            return (short) (POSITION_OPEN + ((short) (this.position * PCT_POSITION_INC)));
        }
    }

    /**
     * Creates the Position representation from KLF code.
     *
     * @param code the coded position
     * @return the velux position representation
     */
    public static Position fromCode(short code) {
        Integer position = (code == POSITION_UNKNOWN) ? null : (code & 0xffff) / PCT_POSITION_INC;
        return new Position(position);
    }
}
