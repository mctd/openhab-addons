package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.status.Position;
import org.openhab.binding.veluxklf200.internal.status.RunStatus;
import org.openhab.binding.veluxklf200.internal.status.Status;
import org.openhab.binding.veluxklf200.internal.status.StatusReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_COMMAND_RUN_STATUS_NTF extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_RUN_STATUS_NTF.class);

    private short sessionID;
    private Status status;
    private byte index;
    private NodeParameter nodeParameter;
    private Position parameterValue;
    private RunStatus runStatus;
    private StatusReply statusReply;

    /**
     * InformationCode contains the hexadecimal
     * information code to show if system is unable to decode status
     */
    private int informationCode;

    public GW_COMMAND_RUN_STATUS_NTF(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.sessionID = this.getCommandFrame().readShort(1);
        this.status = Status.fromCode(this.getCommandFrame().readByte(3));
        this.index = this.getCommandFrame().readByte(4);
        this.nodeParameter = NodeParameter.fromCode(this.getCommandFrame().readByte(5));
        this.parameterValue = Position.fromCode(this.getCommandFrame().readShort(6));
        this.runStatus = RunStatus.fromCode(this.getCommandFrame().readByte(8));
        this.statusReply = StatusReply.fromCode(this.getCommandFrame().readByte(9));
        this.informationCode = this.getCommandFrame().readInt(10);

        logger.info(
                "GW_COMMAND_RUN_STATUS_NTF: sessionID: {}, status: {}, index: {}, nodeParameter: {}, parameterValue: {}, "
                        + "runStatus: {}, statusReply: {}, informationCode: {}",
                this.sessionID, this.status, this.index, this.nodeParameter, this.parameterValue, this.runStatus,
                this.statusReply, this.informationCode);

        // TODO : update position channel : fire event
    }
}
