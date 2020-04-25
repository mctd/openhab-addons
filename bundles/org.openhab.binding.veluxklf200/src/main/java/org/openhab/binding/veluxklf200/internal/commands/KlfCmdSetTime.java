/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import java.time.Instant;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request to set UTC time.
 *
 * @author emmanuel
 */
public class KlfCmdSetTime extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdSetTime.class);

    /**
     * Default constructor.
     *
     */
    public KlfCmdSetTime() {
        super();
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.SET_TIME;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_SET_UTC_CFM:
                this.setStatus(CommandStatus.COMPLETE);
                logger.debug("Set time acknowledged.");
                return true;
            default:
                return false;
        }
    }

    @Override
    protected byte[] pack() {
        return KLFUtils.longToBytes(Instant.now().getEpochSecond());
    }

    @Override
    public boolean isSessionRequired() {
        return false;
    }

    @Override
    public boolean isNodeSpecific() {
        return false;
    }

    @Override
    public KLFGatewayCommands getCommand() {
        return KLFGatewayCommands.GW_SET_UTC_REQ;
    }
}