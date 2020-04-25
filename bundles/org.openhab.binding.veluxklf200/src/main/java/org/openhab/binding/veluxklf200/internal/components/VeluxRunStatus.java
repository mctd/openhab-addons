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
 * Used to indicate the status of a command that is executing.
 *
 * @author emmanuel
 */
public enum VeluxRunStatus {

    /** The execution completed. */
    EXECUTION_COMPLETED((byte) 0),
    /** The execution failed. */
    EXECUTION_FAILED((byte) 1),
    /** The execution active. */
    EXECUTION_ACTIVE((byte) 2),
    /** The unknown. */
    UNKNOWN((byte) 99);

    /** The status code. */
    private byte statusCode;

    /**
     * Instantiates a new Velux run status.
     *
     * @param statusCode
     *            the status code
     */
    private VeluxRunStatus(byte statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public byte getStatusCode() {
        return this.statusCode;
    }

    /**
     * Creates the.
     *
     * @param code
     *            the code
     * @return the Velux run status
     */
    public static VeluxRunStatus createFromCode(byte code) {
        switch (code) {
            case 0:
                return EXECUTION_COMPLETED;
            case 1:
                return EXECUTION_FAILED;
            case 2:
                return EXECUTION_ACTIVE;
            default:
                return UNKNOWN;
        }
    }
}