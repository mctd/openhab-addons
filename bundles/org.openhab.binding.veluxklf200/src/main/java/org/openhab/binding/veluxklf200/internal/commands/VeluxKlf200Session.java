/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class VeluxKlf200Session {

    private static VeluxKlf200Session instance = new VeluxKlf200Session();
    private int session = 1;

    private VeluxKlf200Session() {
        this.session = 0;
    }

    /**
     * Gets the next session identifier.
     *
     * @return the session identifier
     */
    public synchronized int getSessionId() {
        this.session++;

        if (this.session > 0xFFFF) {
            this.session = 1;
        }

        return this.session;
    }

    /**
     * Gets the singleton.
     *
     * @return single instance of {@link VeluxKlf200Session}
     */
    public static VeluxKlf200Session getInstance() {
        return instance;
    }
}