package org.openhab.binding.veluxklf200.internal.commands.response;

import java.util.BitSet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_GET_GROUP_INFORMATION_REQ;
import org.openhab.binding.veluxklf200.internal.commands.status.GroupType;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeVariation;
import org.openhab.binding.veluxklf200.internal.commands.status.Velocity;
import org.openhab.binding.veluxklf200.internal.events.GroupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Acknowledge to {@link GW_GET_GROUP_INFORMATION_REQ}.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_GROUP_INFORMATION_NTF extends BaseNotificationResponse implements GroupEvent {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int groupId; // stored as byte
    private int order; // stored as short
    private int placement; // stored as byte
    private String name;
    private Velocity velocity;
    private NodeVariation nodeVariation;
    private GroupType groupType;
    private int nbrOfObjects; // stored as byte
    private byte[] actuatorBitArray;
    private short revision;

    private BitSet actuators;

    /*
     * private byte[] reverseBitsInArray(byte[] source) {
     * byte[] reversedArray = new byte[source.length];
     * for (int i = 0; i < source.length; i++) {
     * reversedArray[i] = (byte) (Integer.reverse(source[i] & 0xFF) >>> 56);
     * }
     * return reversedArray;
     * }
     *
     * private byte[] getActiveNodes(byte[] bitArray) {
     * byte[] reversed = reverseBitsInArray(bitArray);
     *
     * BitSet b = BitSet.valueOf(reversed);
     * logger.debug("bitset: {}", b);
     * int[] activeNodes = b.stream().toArray();
     *
     * byte[] ret = new byte[activeNodes.length];
     * for (int i = 0; i < activeNodes.length; i++) {
     * ret[i] = (byte) (activeNodes[i] & 0xFF);
     * }
     * return ret;
     * }
     */

    public GW_GET_GROUP_INFORMATION_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.groupId = this.getCommandFrame().readByteAsInt(1);
        this.order = this.getCommandFrame().readShort(2);
        this.placement = this.getCommandFrame().readByteAsInt(4);
        this.name = this.getCommandFrame().readString(5, 64);
        this.velocity = Velocity.fromCode(this.getCommandFrame().readByte(69));
        this.nodeVariation = NodeVariation.fromCode(this.getCommandFrame().readByte(70));
        this.groupType = GroupType.fromCode(this.getCommandFrame().readByte(71));
        this.nbrOfObjects = this.getCommandFrame().readByteAsInt(72);
        this.actuatorBitArray = this.getCommandFrame().readBytes(73, 25);
        this.revision = this.getCommandFrame().readShort(98);

        this.actuators = BitSet.valueOf(actuatorBitArray);

        logger.debug(
                "groupId: {}, order: {}, placement: {}, name: \"{}\", velocity: {}, nodeVariation: {}, "
                        + "groupType: {}, nbrOfObjects: {}, actuators: {}, revision: {}",
                this.getGroupId(), this.getOrder(), this.getPlacement(), this.getName(), this.getVelocity(),
                this.getNodeVariation(), this.getGroupType(), this.getNbrOfObjects(), this.getActuators(),
                this.getRevision());
    }

    @Override
    public int getGroupId() {
        return this.groupId;
    }

    public int getOrder() {
        return order;
    }

    public int getPlacement() {
        return placement;
    }

    public String getName() {
        return name;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public NodeVariation getNodeVariation() {
        return nodeVariation;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public int getNbrOfObjects() {
        return nbrOfObjects;
    }

    public byte[] getActuatorBitArray() {
        return actuatorBitArray;
    }

    public short getRevision() {
        return this.revision;
    }

    public BitSet getActuators() {
        return this.actuators;
    }
}
