package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;
import org.openhab.binding.veluxklf200.internal.commands.status.RunStatus;
import org.openhab.binding.veluxklf200.internal.commands.status.Status;
import org.openhab.binding.veluxklf200.internal.commands.status.StatusReply;
import org.openhab.binding.veluxklf200.internal.events.NodeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For each actuator addressed by IndexArray in the GW_COMMAND_SEND_REQ frame, the gateway will return with two
 * GW_COMMAND_RUN_STATUS_NTF frames. One before and one after the given actuators movement.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_COMMAND_RUN_STATUS_NTF extends BaseNotificationResponse implements NodeEvent {
    private static final Logger logger = LoggerFactory.getLogger(GW_COMMAND_RUN_STATUS_NTF.class);

    /**
     * SessionID are used to identify the command. SessionID has same value as SessionID parameter in the triggering
     * frame
     */
    private int sessionID;
    /** Identification of the status owner */
    private Status status;
    /** Index of the actuator in system table */
    private int nodeId;
    /** Identifies the parameter that ParameterValue carry information about */
    private NodeParameter nodeParameter;
    /** Contains the current value of the active parameter */
    private Position parameterValue;
    /** Contains the execution status of the node */
    private RunStatus runStatus;
    /** Contains current state of the node. (Error code) */
    private StatusReply statusReply;
    /** InformationCode contains the hexadecimal information code to show if system is unable to decode status */
    private int informationCode;

    public GW_COMMAND_RUN_STATUS_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.sessionID = this.getCommandFrame().readShortAsInt(1);
        this.status = Status.fromCode(this.getCommandFrame().readByte(3));
        this.nodeId = this.getCommandFrame().readByteAsInt(4);
        this.nodeParameter = NodeParameter.fromCode(this.getCommandFrame().readByte(5));
        this.parameterValue = Position.fromCode(this.getCommandFrame().readShort(6));
        this.runStatus = RunStatus.fromCode(this.getCommandFrame().readByte(8));
        this.statusReply = StatusReply.fromCode(this.getCommandFrame().readByte(9));
        this.informationCode = this.getCommandFrame().readInt(10);

        logger.debug(
                "GW_COMMAND_RUN_STATUS_NTF: sessionID: {}, status: {}, index: {}, nodeParameter: {}, parameterValue: {}, "
                        + "runStatus: {}, statusReply: {}, informationCode: {}",
                this.sessionID, this.status, this.nodeId, this.nodeParameter, this.parameterValue, this.runStatus,
                this.statusReply, this.informationCode);
    }

    @Override
    public int getNodeId() {
        return this.nodeId;
    }
}
