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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
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

    private static final Logger logger = LoggerFactory.getLogger(Velocity.class);
    private byte code;
    private String description;

    private Velocity(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static Velocity fromCode(byte code) {
        for (Velocity testVal : values()) {
            if (testVal.getCode() == code) {
                return testVal;
            }
        }

        logger.error("Invalid code: {}", code);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}