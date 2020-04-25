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
 * Request version information.
 *
 * @author emmanuel
 */
public class KlfCmdGetVersion extends BaseKLFCommand {

    private Logger logger = LoggerFactory.getLogger(KlfCmdGetVersion.class);
    private String softwareVersion;
    private String hardwareVersion;
    private String productType;
    private String productGroup;
    private final byte REMOTE_CONTROL = 14;
    private final byte KLF200_PRODUCT_TYPE = 3;

    /**
     * Default constructor.
     */
    public KlfCmdGetVersion() {
        super();
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_VERSION_CFM:
                this.softwareVersion = (KLFUtils.extractOneByte(data, FIRSTBYTE) & 0xFF) + ".";
                this.softwareVersion += (KLFUtils.extractOneByte(data, FIRSTBYTE + 1) & 0xFF) + ".";
                this.softwareVersion += (KLFUtils.extractOneByte(data, FIRSTBYTE + 2) & 0xFF) + ".";
                this.softwareVersion += (KLFUtils.extractOneByte(data, FIRSTBYTE + 3) & 0xFF) + ".";
                this.softwareVersion += (KLFUtils.extractOneByte(data, FIRSTBYTE + 4) & 0xFF) + ".";
                this.softwareVersion += (KLFUtils.extractOneByte(data, FIRSTBYTE + 5) & 0xFF);

                this.hardwareVersion = "" + (data[FIRSTBYTE + 6] & 0xFF);

                switch (data[FIRSTBYTE + 7]) {
                    case REMOTE_CONTROL:
                        this.productGroup = "Remote Control";
                        break;
                    default:
                        this.productGroup = "Unknown";
                }

                switch (data[FIRSTBYTE + 8]) {
                    case KLF200_PRODUCT_TYPE:
                        this.productType = "KLF200";
                        break;
                    default:
                        this.productType = "Unknown";
                }
                this.setStatus(CommandStatus.COMPLETE);
                logger.debug("Request version completed. Version is {}", this.getSoftwareVersion());
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets the software version.
     *
     * @return the software version
     */
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * Gets the hardware version.
     *
     * @return the hardware version
     */
    public String getHardwareVersion() {
        return hardwareVersion;
    }

    /**
     * Gets the product type.
     *
     * @return the product type
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Gets the product group.
     *
     * @return the product group
     */
    public String getProductGroup() {
        return productGroup;
    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_VERSION;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
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
        return KLFGatewayCommands.GW_GET_VERSION_REQ;
    }
}