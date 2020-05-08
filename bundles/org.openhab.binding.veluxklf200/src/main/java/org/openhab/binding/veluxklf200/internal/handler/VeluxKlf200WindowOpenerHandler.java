/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Thing;

/**
 * Handles Window Opener Things
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200WindowOpenerHandler extends VeluxKlf200RollerShutterHandler {
    /**
     * Constructor
     *
     * @param thing Thing to Handle
     */
    public VeluxKlf200WindowOpenerHandler(Thing thing) {
        super(thing);
    }

    // TODO : map OPEN/CLOSED and UP/DOWN for sent and received states
}