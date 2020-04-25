package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_GET_VERSION_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_VERSION_CFM.class);

    private byte commandVersionNumber;
    private byte versionWholeNumber;
    private byte versionSubNumber;
    private byte branchID;
    private byte buildNumber;
    private byte microBuild;
    private byte hardwareVersion;
    private byte productGroup;
    private byte productType;

    public GW_GET_VERSION_CFM(KLFCommandFrame commandFrame) {
        super(commandFrame);

        this.commandVersionNumber = this.getCommandFrame().getByte(1);
        this.versionWholeNumber = this.getCommandFrame().getByte(2);
        this.versionSubNumber = this.getCommandFrame().getByte(3);
        this.branchID = this.getCommandFrame().getByte(4);
        this.buildNumber = this.getCommandFrame().getByte(5);
        this.microBuild = this.getCommandFrame().getByte(6);
        this.hardwareVersion = this.getCommandFrame().getByte(7);
        this.productGroup = this.getCommandFrame().getByte(8);
        this.productType = this.getCommandFrame().getByte(9);

        logger.info("KLF version: {}.{}.{}.{}.{}.{}. Hardware version: {}, product group: {}, product type: {}",
                this.commandVersionNumber, versionWholeNumber, versionSubNumber, branchID, buildNumber, microBuild,
                hardwareVersion, productGroup, productType);
    }
}
