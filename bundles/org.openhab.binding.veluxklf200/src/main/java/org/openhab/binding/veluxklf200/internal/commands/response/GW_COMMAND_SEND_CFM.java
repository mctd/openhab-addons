package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_COMMAND_SEND_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_SEND_CFM.class);

    private short sessionId;
    private CommandStatus status;

    public GW_COMMAND_SEND_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.sessionId = this.getCommandFrame().readShort(1);
        this.status = CommandStatus.fromCode(this.getCommandFrame().readByte(3));

        logger.debug("Command result for session {}: {}", this.sessionId, this.status);
    }

    public short getSessionId() {
        return sessionId;
    }

    public CommandStatus getStatus() {
        return status;
    }
}
