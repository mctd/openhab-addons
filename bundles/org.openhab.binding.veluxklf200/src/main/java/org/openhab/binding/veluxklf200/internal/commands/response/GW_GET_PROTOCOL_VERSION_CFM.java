package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_PROTOCOL_VERSION_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_PROTOCOL_VERSION_CFM.class);

    private int majorVersion;
    private int minorVersion;

    public GW_GET_PROTOCOL_VERSION_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.majorVersion = this.getCommandFrame().readShortAsInt(1);
        this.minorVersion = this.getCommandFrame().readShortAsInt(3);

        logger.debug("Major version: {}, minor version: {}", this.getMajorVersion(), this.getMinorVersion());
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public String getFullVersion() {
        return String.format("%d.%d", this.getMajorVersion(), this.getMinorVersion());
    }
}
