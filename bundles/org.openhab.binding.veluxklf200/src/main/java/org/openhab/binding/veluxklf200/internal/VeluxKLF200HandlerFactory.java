/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
//import org.openhab.binding.veluxklf200.internal.VeluxKLF200V2Handler;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.veluxklf200.internal.discovery.KLF200DiscoveryService;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200GroupHandler;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200BridgeHandler;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200SceneHandler;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200RollerShutterHandler;
import org.openhab.binding.veluxklf200.internal.handler.VeluxKlf200WindowOpenerHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible of creating things and thing handlers.
 *
 * @author emmanuel - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding." + VeluxKlf200BindingConstants.BINDING_ID, service = ThingHandlerFactory.class)
public class VeluxKLF200HandlerFactory extends BaseThingHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** A registry of things we have discovered. */
    private final Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return VeluxKlf200BindingConstants.SUPPORTED_VELUX_KLF200_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        logger.debug("Trying to create a handler for Thing: {}", thing.getUID());

        // Creates a Bridge handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_KLF200)) {
            VeluxKlf200BridgeHandler handler = new VeluxKlf200BridgeHandler((Bridge) thing);
            registerDiscoveryService(handler);
            return handler;
        }

        // Creates a Roller Shutter handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_ROLLER_SHUTTER)) {
            return new VeluxKlf200RollerShutterHandler(thing);
        }

        // Creates a Window Opener handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_WINDOW_OPENER)) {
            return new VeluxKlf200WindowOpenerHandler(thing);
        }

        // Creates a Scene handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_SCENE)) {
            return new VeluxKlf200SceneHandler(thing);
        }

        // Creates a Group handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_GROUP)) {
            return new VeluxKlf200GroupHandler(thing);
        }

        // Creates a Generic Actuator handler
        if (thingTypeUID.equals(VeluxKlf200BindingConstants.THING_TYPE_ACTUATOR)) {
            // TODO : build a true generic actuator handler
            return new VeluxKlf200RollerShutterHandler(thing);
        }

        logger.error("Failed to create an handler. Unsupported Thing type: {}", thingTypeUID);
        return null;
    }

    /**
     * Registers a discovery service for a bridge handler.
     *
     * @param bridgeHandler handler to register service for
     */
    private synchronized void registerDiscoveryService(VeluxKlf200BridgeHandler bridgeHandler) {
        logger.debug("Registering discovery service for the KLF200 bridgeHandler");
        KLF200DiscoveryService discoveryService = new KLF200DiscoveryService(bridgeHandler);
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        logger.debug("Removing handler: {}.", thingHandler);
        if (thingHandler instanceof VeluxKlf200BridgeHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            serviceReg.unregister();
        }
    }
}
