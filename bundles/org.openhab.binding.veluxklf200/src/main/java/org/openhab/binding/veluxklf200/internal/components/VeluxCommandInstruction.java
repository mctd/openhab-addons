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
 * Provides the information / parameters required to execute a command.
 *
 * @author emmanuel
 */
public class VeluxCommandInstruction {

    private byte function;
    private short position;
    private byte nodeId;

    /**
     * Instantiates a new Velux command instruction.
     *
     * @param nodeId
     *            the node ID to operate
     * @param function
     *            the functional parameter to operate
     * @param position
     *            the position
     */
    public VeluxCommandInstruction(byte nodeId, byte function, short position) {
        this.nodeId = nodeId;
        this.function = function;
        this.position = position;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public byte getNodeId() {
        return nodeId;
    }

    /**
     * Gets the functional parameter to operate.
     *
     * @return the function
     */
    public byte getFunction() {
        return function;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public short getPosition() {
        return position;
    }
}