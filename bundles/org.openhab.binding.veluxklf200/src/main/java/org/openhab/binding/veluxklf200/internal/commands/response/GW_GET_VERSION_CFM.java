package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.ProductGroup;
import org.openhab.binding.veluxklf200.internal.commands.status.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_VERSION_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_VERSION_CFM.class);

    private int commandVersionNumber;
    private int versionWholeNumber;
    private int versionSubNumber;
    private int branchID;
    private int buildNumber;
    private int microBuild;
    private int hardwareVersion;
    private ProductGroup productGroup;
    private ProductType productType;

    public GW_GET_VERSION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);

        this.commandVersionNumber = this.getCommandFrame().readByteAsInt(1);
        this.versionWholeNumber = this.getCommandFrame().readByteAsInt(2);
        this.versionSubNumber = this.getCommandFrame().readByteAsInt(3);
        this.branchID = this.getCommandFrame().readByteAsInt(4);
        this.buildNumber = this.getCommandFrame().readByteAsInt(5);
        this.microBuild = this.getCommandFrame().readByteAsInt(6);
        this.hardwareVersion = this.getCommandFrame().readByteAsInt(7);
        this.productGroup = ProductGroup.fromCode(this.getCommandFrame().readByte(8));
        this.productType = ProductType.fromCode(this.getCommandFrame().readByte(9));

        logger.trace(
                "Received GW_GET_VERSION_CFM. KLF version: {}.{}.{}.{}.{}.{}. Hardware version: {}, product group: {}, product type: {}",
                this.commandVersionNumber, versionWholeNumber, versionSubNumber, branchID, buildNumber, microBuild,
                hardwareVersion, productGroup, productType);
    }

    public String getSofwareVersion() {
        return String.format("%s.%s.%s.%s.%s.%s", this.commandVersionNumber, this.versionWholeNumber,
                this.versionSubNumber, this.branchID, this.buildNumber, this.microBuild);
    }

    public int getHardwareVersion() {
        return this.hardwareVersion;
    }

    public ProductGroup getProductGroup() {
        return this.productGroup;
    }

    public ProductType getProductType() {
        return this.productType;
    }
}
