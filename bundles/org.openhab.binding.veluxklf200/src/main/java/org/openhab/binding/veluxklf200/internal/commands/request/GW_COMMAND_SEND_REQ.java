package org.openhab.binding.veluxklf200.internal.commands.request;

import java.util.BitSet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.VeluxKlf200Session;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_COMMAND_SEND_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.status.CommandOriginator;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;
import org.openhab.binding.veluxklf200.internal.commands.status.PriorityLevelLock;

@NonNullByDefault
public class GW_COMMAND_SEND_REQ extends BaseRequest<GW_COMMAND_SEND_CFM> {
    private int sessionId;
    private CommandOriginator commandOriginator = CommandOriginator.USER;
    private byte priorityLevel = 3; // User Level 2 (default)
    private NodeParameter parameterActive = NodeParameter.MP; // Default to MP
    private byte fPI1;
    private byte fPI2;
    private byte indexArrayCount; // from 1 to 20
    /**
     * 20 byte long array indicating nodes in the system table. One byte for each node, each byte in array can have
     * value
     * [0;199]
     */
    private byte[] indexArray = new byte[20];
    private PriorityLevelLock priorityLevelLock = PriorityLevelLock.NO_LOCK;
    private byte pL_0_3 = 0; // make a class to abstract the 2 priority bytes?
    private byte pL_4_7 = 0;
    /**
     * LockTime defines a common lock time for all priority levels. 0 => 30 seconds ; 1 => 60 seconds ; 254 => 7650
     * seconds ; 255 => unlimited time
     */
    private byte lockTime = 0;
    private ParameterValues paramValues;

    public GW_COMMAND_SEND_REQ(int nodeId, NodeParameter parameter, Position position) {
        this(new int[] { nodeId }, parameter, position);
    }

    public GW_COMMAND_SEND_REQ(int[] nodeIds, NodeParameter parameter, Position position) {
        super(GatewayCommands.GW_COMMAND_SEND_REQ);

        // TODO : validate nodeIds length >= 1 and <= 20

        this.sessionId = VeluxKlf200Session.getInstance().getSessionId();
        for (int i = 0; i < nodeIds.length; i++) {
            indexArray[i] = (byte) (nodeIds[i] & 0xFF);
        }
        this.indexArrayCount = (byte) nodeIds.length; // real number of parameters sent
        this.paramValues = new ParameterValues().setPosition(parameter, position);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // TODO : validate paramValues.length >= 1

        // Converts activated FPs to a BIG ENDIAN bit word
        BitSet fpBits = new BitSet(16);
        for (byte i = 1; i <= 16; i++) {
            if (this.paramValues.isParameterSet(i)) {
                fpBits.set(16 - i);
            }
        }
        byte[] fpBitsArray = fpBits.toByteArray();
        this.fPI1 = fpBitsArray.length > 1 ? fpBitsArray[1] : 0;
        this.fPI2 = fpBitsArray.length > 0 ? fpBitsArray[0] : 0;

        // Write command frame data
        commandFrame.writeUnsignedShort(this.sessionId); // session
        commandFrame.writeByte(this.commandOriginator.getCode()); // command originator
        commandFrame.writeByte(this.priorityLevel); // priority level
        commandFrame.writeByte(this.parameterActive.getCode()); // parameter active

        commandFrame.writeByte(this.fPI1); // FPI1
        commandFrame.writeByte(this.fPI2); // FPI2

        // FunctionalParameterValueArray has room for 17 parameter values. Position 0 is the MP value. Position 1 to 16
        // holds Functional Parameter 1 to 16.
        /*
         * for (short position : this.paramValues.toShortArray()) {
         * commandFrame.writeShort(position);
         * }
         */
        for (Position p : this.paramValues.getPositions()) {
            // Writes 0 if position not set, else position code as short
            commandFrame.writeShort(p == null ? 0 : p.getCode());
        }

        commandFrame.writeByte(this.indexArrayCount); // IndexArrayCount
        for (byte b : this.indexArray) { // 20 bytes long
            commandFrame.writeByte(b);
        }

        commandFrame.writeByte(this.priorityLevelLock.getCode()); // PriorityLevelLock
        commandFrame.writeByte(this.pL_0_3); // PL_0_3
        commandFrame.writeByte(this.pL_4_7); // PL_4_7
        commandFrame.writeByte(this.lockTime); // LockTime
    }

    public GW_COMMAND_SEND_REQ setParamValue(NodeParameter parameter, Position position) {
        this.paramValues.setPosition(parameter, position);
        return this;
    }

    @Override
    public boolean handleResponseImpl(GW_COMMAND_SEND_CFM response) {
        if (this.sessionId == response.getSessionId()) {
            return true;
        } else {
            return false;
        }
    }

    public GW_COMMAND_SEND_REQ setParameterActive(NodeParameter parameterActive) {
        this.parameterActive = parameterActive;
        return this;
    }
}
