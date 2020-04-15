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
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request KLF 200 API protocol version.
 *
 * @author emmanuel
 */
public class KlfCmdGetProtocol extends BaseKLFCommand {

    private Logger logger = LoggerFactory.getLogger(KlfCmdGetProtocol.class);
    private String protocol;

    /**
     * Default constructor.
     */
    public KlfCmdGetProtocol() {
        super();
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_PROTOCOL_VERSION_CFM:
                this.protocol = "" + KLFUtils.extractTwoBytes(data, FIRSTBYTE);
                this.protocol += "." + KLFUtils.extractTwoBytes(data, FIRSTBYTE + 2);
                this.commandStatus = CommandStatus.COMPLETE;
                logger.debug("Get protocol command completed. Protocol is {}", this.getProtocol());
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_PROTOCOL;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}