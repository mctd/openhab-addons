package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.status.CommandStatus;
import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_COMMAND_SEND_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_SEND_CFM.class);

    private short sessionID;
    private CommandStatus status;

    public GW_COMMAND_SEND_CFM(KLFCommandFrame commandFrame) {
        super(commandFrame);
        this.sessionID = this.getCommandFrame().getShort(1);
        this.status = CommandStatus.fromCode(this.getCommandFrame().getByte(3));

        if (this.status == CommandStatus.ACCEPTED) {
            logger.info("Command accepted for session: {}.", this.sessionID);
        } else {
            logger.error("Command rejected for session: {}.", this.sessionID);
        }
    }
}
