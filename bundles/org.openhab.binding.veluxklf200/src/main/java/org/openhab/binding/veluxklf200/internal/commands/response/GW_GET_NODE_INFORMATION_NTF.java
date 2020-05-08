package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.ExecutionState;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeTypeSubType;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeVariation;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;
import org.openhab.binding.veluxklf200.internal.commands.status.PowerMode;
import org.openhab.binding.veluxklf200.internal.commands.status.ProductGroup;
import org.openhab.binding.veluxklf200.internal.commands.status.ProductType;
import org.openhab.binding.veluxklf200.internal.commands.status.Velocity;
import org.openhab.binding.veluxklf200.internal.events.NodePositionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_GET_NODE_INFORMATION_NTF extends BaseNotificationResponse implements NodePositionEvent {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int nodeId;
    private int order;
    private int placement;
    private String name;
    private Velocity velocity;
    private NodeTypeSubType nodeTypeSubType;
    private ProductGroup productGroup;
    private ProductType productType;
    private NodeVariation nodeVariation;
    private PowerMode powerMode;
    private int buildNumber;
    private long serialNumber;
    private ExecutionState executionState;
    private Position currentPosition;
    private Position target;
    private Position fp1currentPosition;
    private Position fp2currentPosition;
    private Position fp3currentPosition;
    private Position fp4currentPosition;
    private int remainingTime;
    private int timeStamp;
    private byte nbrOfAlias; // max number of alias is 5
    private byte[] aliasArray;

    public GW_GET_NODE_INFORMATION_NTF(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.nodeId = this.getCommandFrame().readByteAsInt(1);
        this.order = this.getCommandFrame().readShortAsInt(2);
        this.placement = this.getCommandFrame().readByteAsInt(4);
        this.name = this.getCommandFrame().readString(5, 64);
        this.velocity = Velocity.fromCode(this.getCommandFrame().readByte(69));
        this.nodeTypeSubType = NodeTypeSubType.fromCode(this.getCommandFrame().readShort(70));
        this.productGroup = ProductGroup.fromCode(this.getCommandFrame().readByte(72));
        this.productType = ProductType.fromCode(this.getCommandFrame().readByte(73));
        this.nodeVariation = NodeVariation.fromCode(this.getCommandFrame().readByte(74));
        this.powerMode = PowerMode.fromCode(this.getCommandFrame().readByte(75));
        this.buildNumber = this.getCommandFrame().readByteAsInt(76);
        this.serialNumber = this.getCommandFrame().readLong(77);
        this.executionState = ExecutionState.fromCode(this.getCommandFrame().readByte(85));
        this.currentPosition = Position.fromCode(this.getCommandFrame().readShort(86));
        this.target = Position.fromCode(this.getCommandFrame().readShort(88));
        this.fp1currentPosition = Position.fromCode(this.getCommandFrame().readShort(90));
        this.fp2currentPosition = Position.fromCode(this.getCommandFrame().readShort(92));
        this.fp3currentPosition = Position.fromCode(this.getCommandFrame().readShort(94));
        this.fp4currentPosition = Position.fromCode(this.getCommandFrame().readShort(96));
        this.remainingTime = this.getCommandFrame().readShortAsInt(98);
        this.timeStamp = this.getCommandFrame().readInt(100);
        this.nbrOfAlias = this.getCommandFrame().readByte(104);
        this.aliasArray = this.getCommandFrame().readBytes(105, 20);

        // TODO : convert aliasArray to a better object representation

        logger.debug(
                "nodeID: {}, order: {}, placement: {}, name: \"{}\", velocity: {}, nodeTypeSubType: {}, productGroup: {}, productType: {}, "
                        + "nodeVariation: {}, powerMode: {}, buildNumber: {}, serialNumber: {}, state: {}, currentPosition: {}, target: {}, fp1currentPosition: {}, "
                        + "fp2currentPosition: {}, fp3currentPosition: {}, fp4currentPosition: {}, remainingTime: {}, timeStamp: {}, nbrOfAlias: {}, aliasArray: {}",
                this.getNodeId(), this.getOrder(), this.getPlacement(), this.getName(), this.getVelocity(),
                this.getNodeTypeSubType(), this.getProductGroup(), this.getProductType(), this.getNodeVariation(),
                this.getPowerMode(), this.getBuildNumber(), this.getSerialNumber(), this.getExecutionState(),
                this.getCurrentPosition(), this.getTarget(), this.getFp1currentPosition(), this.getFp2currentPosition(),
                this.getFp3currentPosition(), this.getFp4currentPosition(), this.getRemainingTime(),
                this.getTimeStamp(), this.getNbrOfAlias(), this.getAliasArray());
    }

    @Override
    public int getNodeId() {
        return this.nodeId;
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

    public NodeTypeSubType getNodeTypeSubType() {
        return nodeTypeSubType;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public ProductType getProductType() {
        return productType;
    }

    public NodeVariation getNodeVariation() {
        return nodeVariation;
    }

    public PowerMode getPowerMode() {
        return powerMode;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    @Override
    public ExecutionState getExecutionState() {
        return executionState;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Position getTarget() {
        return target;
    }

    @Override
    public Position getFp1currentPosition() {
        return fp1currentPosition;
    }

    @Override
    public Position getFp2currentPosition() {
        return fp2currentPosition;
    }

    @Override
    public Position getFp3currentPosition() {
        return fp3currentPosition;
    }

    @Override
    public Position getFp4currentPosition() {
        return fp4currentPosition;
    }

    @Override
    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public int getTimeStamp() {
        return timeStamp;
    }

    public byte getNbrOfAlias() {
        return nbrOfAlias;
    }

    public byte[] getAliasArray() {
        return aliasArray;
    }
}
