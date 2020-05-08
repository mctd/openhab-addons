/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link VeluxKlf200BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200BindingConstants {

    /**
     * ID of the binding
     */
    public static final String BINDING_ID = "veluxklf200";

    // List of all Thing Type UIDs
    /** KLF200 Bridge */
    public static final ThingTypeUID THING_TYPE_KLF200 = new ThingTypeUID(BINDING_ID, "klf200");

    /** Scene */
    public static final ThingTypeUID THING_TYPE_SCENE = new ThingTypeUID(BINDING_ID, "scene");

    /** Group */
    public static final ThingTypeUID THING_TYPE_GROUP = new ThingTypeUID(BINDING_ID, "group");

    /** Roller Shutter */
    public static final ThingTypeUID THING_TYPE_ROLLER_SHUTTER = new ThingTypeUID(BINDING_ID, "roller_shutter");

    /** Window Opener */
    public static final ThingTypeUID THING_TYPE_WINDOW_OPENER = new ThingTypeUID(BINDING_ID, "window_opener");

    /** Generic Actuator */
    public static final ThingTypeUID THING_TYPE_ACTUATOR = new ThingTypeUID(BINDING_ID, "actuator");

    // List of all Channel ids
    // Scene trigger
    public static final String CHANNEL_TRIGGER_SCENE = "trigger_scene";

    // Roller shutter, Window Opener
    public static final String CHANNEL_CONTROL = "control";

    // List of all properties
    // Velocity
    public static final String PROP_VELOCITY = "velocity";

    /** All the supported thing types */
    public static final Set<ThingTypeUID> SUPPORTED_VELUX_KLF200_THING_TYPES_UIDS = new HashSet<>(
            Arrays.asList(THING_TYPE_KLF200, THING_TYPE_SCENE, THING_TYPE_GROUP, THING_TYPE_ROLLER_SHUTTER,
                    THING_TYPE_WINDOW_OPENER, THING_TYPE_ACTUATOR));
}
