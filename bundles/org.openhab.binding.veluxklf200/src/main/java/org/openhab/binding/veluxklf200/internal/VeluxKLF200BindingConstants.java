/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link VeluxKLF200BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author mctd - Initial contribution
 */
@NonNullByDefault
public class VeluxKLF200BindingConstants {

    private static final String BINDING_ID = "veluxklf200";

    // List of all Thing Type UIDs
    /** BRIDGE. */
    public static final ThingTypeUID THING_TYPE_VELUX_KLF200 = new ThingTypeUID(BINDING_ID, "klf200-bridge");

    /** SCENES */
    public static final ThingTypeUID THING_TYPE_VELUX_SCENE = new ThingTypeUID(BINDING_ID, "velux_scene");

    /** ACTUATOR */
    public static final ThingTypeUID THING_TYPE_ACTUATOR = new ThingTypeUID(BINDING_ID, "velux_actuator");

    // List of all Channel ids
    /** Trigger a scene */
    public static final String KLF200_TRIGGER_SCENE = "trigger_scene";

    /** Position of a roller shutter. */
    public static final String ACTUATOR_MP_POSITION_CHANNEL_ID = "mp_position";

    /** All the supported thing types */
    public static final Set<ThingTypeUID> SUPPORTED_VELUX_KLF200_THING_TYPES_UIDS = Collections.unmodifiableSet(Stream
            .of(THING_TYPE_VELUX_KLF200, THING_TYPE_VELUX_SCENE, THING_TYPE_ACTUATOR).collect(Collectors.toSet()));
    // TODO : add GROUPS

    public static final String ACTUATOR_PROP_PRODUCT_TYPE = "product_type";
}
