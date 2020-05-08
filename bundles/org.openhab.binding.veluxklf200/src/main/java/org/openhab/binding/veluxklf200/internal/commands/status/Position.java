/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Translates KLF position values to human readable values.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class Position {
    private static final short POSITION_OPEN = (short) 0x0000;
    private static final short PCT_POSITION_INC = (short) 0x200;
    private static final short POSITION_UNKNOWN = (short) 0xF7FF;
    private static final short POSITION_STOP = (short) 0xD200;

    public static final Position STOP = new Position(POSITION_STOP);

    private @Nullable Integer position;
    private short code;

    public Position(@Nullable Integer position) {
        if (position == null) {
            this.position = position;
        } else if (position <= 0) {
            this.position = 0;
        } else if (position >= 100) {
            this.position = 100;
        } else {
            this.position = position;
        }

        Integer localPosition = this.position;
        if (localPosition == null) {
            this.code = POSITION_UNKNOWN;
        } else {
            this.code = (short) (POSITION_OPEN + ((short) (localPosition * PCT_POSITION_INC)));
        }
    }

    private Position(short code) {
        this.code = code;
    }

    // TODO : create constructor from UP / DOWN, OPEN/ CLOSED, etc. ?

    @Override
    public String toString() {
        Integer localPosition = this.position;
        if (localPosition == null) {
            return "Unknown";
        }
        return localPosition.toString();
    }

    /**
     * Gets the percentage closed.
     *
     * @return the percentage closed, null if unknown
     */
    public @Nullable Integer getPosition() {
        return this.position;
    }

    public short getCode() {
        return this.code;
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
