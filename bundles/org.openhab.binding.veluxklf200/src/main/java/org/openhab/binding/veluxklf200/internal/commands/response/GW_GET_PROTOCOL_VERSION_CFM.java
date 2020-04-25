package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_GET_PROTOCOL_VERSION_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_PROTOCOL_VERSION_CFM.class);

    private short majorVersion;
    private short minorVersion;

    public GW_GET_PROTOCOL_VERSION_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.majorVersion = this.getCommandFrame().getShort(1);
        this.minorVersion = this.getCommandFrame().getShort(3);

        logger.info("Major version: {}, minor version: {}", this.getMajorVersion(), this.getMinorVersion());
    }

    public short getMajorVersion() {
        return this.majorVersion;
    }

    public short getMinorVersion() {
        return this.minorVersion;
    }
}
