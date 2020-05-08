/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Helper functions.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200Helpers {
    /**
     * Formats a byte array as an hex string.
     *
     * @param array the byte array.
     * @return the hex string representing the array.
     */
    public static String byteArrayToHexString(byte[] array) {
        ArrayList<String> stringArray = new ArrayList<String>();

        for (byte b : array) {
            stringArray.add(String.format("%02X", b));
        }
        return String.format("[%s]", String.join(", ", stringArray));
    }
}