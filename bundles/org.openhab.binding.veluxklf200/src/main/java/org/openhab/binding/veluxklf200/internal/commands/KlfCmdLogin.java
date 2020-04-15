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
 * Enter password to authenticate request.
 *
 * @author emmanuel
 */
public class KlfCmdLogin extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdLogin.class);
    private String password;

    /**
     * Default constructor.
     *
     * @param password
     *            Password for the KLF200 unit.
     */
    public KlfCmdLogin(String password) {
        super();
        this.password = password;
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.LOGIN;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_PASSWORD_ENTER_CFM:
                logger.trace("Handling GW_PASSWORD_ENTER_CFM with payload {}", KLFUtils.formatBytes(data));
                if (data[FIRSTBYTE] == 0) {
                    // Authentication was successful.
                    this.commandStatus = CommandStatus.COMPLETE;
                } else {
                    // Authentication failed.
                    this.commandStatus = CommandStatus.ERROR;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isValid() {
        if ((null == this.password) || (this.password.length() < 1) || (this.password.length() > 32)) {
            logger.error("The password is not valid.");
            return false;
        }
        return true;
    }

    @Override
    protected byte[] pack() {
        byte[] data = new byte[32];
        byte[] password = this.password.getBytes();
        if (password.length > data.length) {
            logger.error(
                    "Password specified is longer ({} characters) than the maximum lenght (32 characters) permissible.",
                    password.length);
            return null;
        }
        System.arraycopy(password, 0, data, 0, password.length);
        return data;
    }
}