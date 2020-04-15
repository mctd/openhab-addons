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
 * Enables HomeStatus monitor.
 *
 * @author emmanuel
 */
public class KlfCmdEnableHomeStatusMonitor extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdEnableHomeStatusMonitor.class);

    /**
     * Default constructor.
     *
     */
    public KlfCmdEnableHomeStatusMonitor() {
        super();
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.ENABLE_HOUSE_STATUS_MONITOR;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_HOUSE_STATUS_MONITOR_ENABLE_CFM:
                logger.debug("Home Status Monitor enal.");
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            default:
                return false;
        }
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}