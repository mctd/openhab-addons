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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.openhab.binding.veluxklf200.internal.commands.request.BaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to all handlers
 *
 * @author emmanuel
 */
@NonNullByDefault
public abstract class VeluxKlf200BaseThingHandler extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(VeluxKlf200BaseThingHandler.class);

    /**
     * Constructor
     *
     * @param thing The thing
     */
    public VeluxKlf200BaseThingHandler(Thing thing) {
        super(thing);
    }

    public @Nullable ThingUID getBridgeUID() {
        Bridge b = this.getBridge();
        return b == null ? null : b.getUID();
    }

    /**
     * Gets the bridge handler.
     *
     * @return the bridge handler.
     */
    protected @Nullable VeluxKlf200BridgeHandler getBridgeHandler() {
        Bridge b = this.getBridge();
        return b == null ? null : (VeluxKlf200BridgeHandler) b.getHandler();
    }

    protected void sendRequest(BaseRequest<?> request) {
        VeluxKlf200BridgeHandler bridgeHandler = this.getBridgeHandler();
        if (bridgeHandler != null) {
            bridgeHandler.getConnection().sendRequest(request);
        } else {
            logger.warn("Bridge Handler is null, cannot execute command.");
        }
    }
}