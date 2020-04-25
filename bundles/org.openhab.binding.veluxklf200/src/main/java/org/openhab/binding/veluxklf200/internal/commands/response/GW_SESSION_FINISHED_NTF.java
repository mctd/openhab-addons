package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sent when the session started by GW_COMMAND_SEND_REQ, is over.
 *
 * @author emmanuel
 *
 */
public class GW_SESSION_FINISHED_NTF extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_SESSION_FINISHED_NTF.class);

    private short sessionID;

    public GW_SESSION_FINISHED_NTF(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.sessionID = this.getCommandFrame().getShort(1);

        logger.info("GW_SESSION_FINISHED_NTF: Command with sessionID {} is over.", this.getSessionID());
    }

    public short getSessionID() {
        return this.sessionID;
    }
}
