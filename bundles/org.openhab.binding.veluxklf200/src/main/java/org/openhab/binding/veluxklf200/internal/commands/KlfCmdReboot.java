/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reboots the KLF200 unit.
 *
 * @author emmanuel
 */
public class KlfCmdReboot extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdReboot.class);

    /**
     * Default constructor.
     *
     */
    public KlfCmdReboot() {
        super();
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_REBOOT_CFM:
                logger.debug("Reboot accepted.");
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.REBOOT;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}