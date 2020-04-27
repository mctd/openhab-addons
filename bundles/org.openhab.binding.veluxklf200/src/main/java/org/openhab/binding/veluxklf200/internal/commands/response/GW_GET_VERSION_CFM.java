package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
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

    public GW_GET_VERSION_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);

        this.commandVersionNumber = this.getCommandFrame().readByte(1);
        this.versionWholeNumber = this.getCommandFrame().readByte(2);
        this.versionSubNumber = this.getCommandFrame().readByte(3);
        this.branchID = this.getCommandFrame().readByte(4);
        this.buildNumber = this.getCommandFrame().readByte(5);
        this.microBuild = this.getCommandFrame().readByte(6);
        this.hardwareVersion = this.getCommandFrame().readByte(7);
        this.productGroup = this.getCommandFrame().readByte(8);
        this.productType = this.getCommandFrame().readByte(9);

        logger.info("KLF version: {}.{}.{}.{}.{}.{}. Hardware version: {}, product group: {}, product type: {}",
                this.commandVersionNumber, versionWholeNumber, versionSubNumber, branchID, buildNumber, microBuild,
                hardwareVersion, productGroup, productType);
    }
}
