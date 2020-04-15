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
 * Request the state of the gateway. Used as a keepalive ping.
 *
 * @author emmanuel
 */
public class KlfCmdPing extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdPing.class);
    private String gatewayState;
    private String gatewaySubState;

    /**
     * Default constructor.
     */
    public KlfCmdPing() {
        super();
    }

    /**
     * Gets the gateway state.
     *
     * @return the gateway state
     */
    public String getGatewayState() {
        return gatewayState + "::" + gatewaySubState;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_STATE_CFM:
                switch (data[FIRSTBYTE]) {
                    case 0x0:
                        gatewayState = "Test mode";
                        break;
                    case 0x1:
                        gatewayState = "Gateway mode, no actuator nodes in the system table";
                        break;
                    case 0x2:
                        gatewayState = "Gateway mode, with one or more actuator nodes in the system table";
                        break;
                    case 0x3:
                        gatewayState = "Beacon mode, not configured by a remote controller";
                        break;
                    case 0x4:
                        gatewayState = "Beacon mode, has been configured by a remote controller";
                        break;
                    default:
                        gatewayState = "Unknown";
                        break;
                }
                switch (data[FIRSTBYTE + 1]) {
                    case 0x0:
                        gatewaySubState = "Idle state";
                        break;
                    case 0x1:
                        gatewaySubState = "Performing task in Configuration Service handler";
                        break;
                    case 0x2:
                        gatewaySubState = "Performing Scene Configuration";
                        break;
                    case 0x3:
                        gatewaySubState = "Performing Information Service Configuration";
                        break;
                    case 0x4:
                        gatewaySubState = "Performing Contact input Configuration";
                        break;
                    case (byte) 0x80:
                        gatewaySubState = "Performing task in Command Handler";
                        break;
                    case (byte) 0x81:
                        gatewaySubState = "Performing task in Activate Group Handler";
                        break;
                    case (byte) 0x82:
                        gatewaySubState = "Performing task in Activate Scene Handler";
                        break;
                    default:
                        gatewaySubState = "Unknown";
                        break;
                }
                this.commandStatus = CommandStatus.COMPLETE;
                logger.debug("Get state successful. State: {}", this.getGatewayState());
                return true;
            default:
                return false;
        }
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.PING;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}