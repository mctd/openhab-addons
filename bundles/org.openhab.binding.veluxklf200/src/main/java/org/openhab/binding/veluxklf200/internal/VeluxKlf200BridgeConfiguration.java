/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal;

/**
 * KLF200 unit configuration.
 *
 * @author emmanuel
 */
public class VeluxKlf200BridgeConfiguration {
    /** Hostname or IP address of the KLF200 unit. */
    public String hostname;

    /** API port */
    public Integer port;

    /** API password */
    public String password;

    /** Duration (in minutes) between 2 pings will be sent to keep the connection alive */
    public Integer keepalive;
}
