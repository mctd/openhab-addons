package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.NodeTypeSubType;
import org.openhab.binding.veluxklf200.internal.status.NodeVariation;
import org.openhab.binding.veluxklf200.internal.status.Position;
import org.openhab.binding.veluxklf200.internal.status.PowerMode;
import org.openhab.binding.veluxklf200.internal.status.ProductType;
import org.openhab.binding.veluxklf200.internal.status.State;
import org.openhab.binding.veluxklf200.internal.status.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_GET_NODE_INFORMATION_NTF extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_NODE_INFORMATION_NTF.class);

    private byte nodeID;
    private short order;
    private byte placement;
    private String name;
    private Velocity velocity;
    private NodeTypeSubType nodeTypeSubType;
    private byte productGroup;
    private ProductType productType;
    private NodeVariation nodeVariation;
    private PowerMode powerMode;
    private byte buildNumber;
    private long serialNumber;
    private State state;
    private Position currentPosition;
    private Position target;
    private Position fp1currentPosition;
    private Position fp2currentPosition;
    private Position fp3currentPosition;
    private Position fp4currentPosition;
    private short remainingTime;
    private int timeStamp;
    private byte nbrOfAlias;
    private byte[] aliasArray;

    public GW_GET_NODE_INFORMATION_NTF(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.nodeID = this.getCommandFrame().getByte(1);
        this.order = this.getCommandFrame().getShort(2);
        this.placement = this.getCommandFrame().getByte(4);
        this.name = this.getCommandFrame().getString(5, 64);
        this.velocity = Velocity.fromCode(this.getCommandFrame().getByte(69));
        this.nodeTypeSubType = NodeTypeSubType.fromCode(this.getCommandFrame().getShort(70));
        this.productGroup = this.getCommandFrame().getByte(72);
        this.productType = ProductType.fromCode(this.getCommandFrame().getByte(73));
        this.nodeVariation = NodeVariation.fromCode(this.getCommandFrame().getByte(74));
        this.powerMode = PowerMode.fromCode(this.getCommandFrame().getByte(75));
        this.buildNumber = this.getCommandFrame().getByte(76);
        this.serialNumber = this.getCommandFrame().getLong(77);
        this.state = State.fromCode(this.getCommandFrame().getByte(85));
        this.currentPosition = Position.fromCode(this.getCommandFrame().getShort(86));
        this.target = Position.fromCode(this.getCommandFrame().getShort(88));
        this.fp1currentPosition = Position.fromCode(this.getCommandFrame().getShort(90));
        this.fp2currentPosition = Position.fromCode(this.getCommandFrame().getShort(92));
        this.fp3currentPosition = Position.fromCode(this.getCommandFrame().getShort(94));
        this.fp4currentPosition = Position.fromCode(this.getCommandFrame().getShort(96));
        this.remainingTime = this.getCommandFrame().getShort(98);
        this.timeStamp = this.getCommandFrame().getInt(100);
        this.nbrOfAlias = this.getCommandFrame().getByte(104);
        this.aliasArray = this.getCommandFrame().getBytes(105, 20);

        logger.info(
                "GW_GET_NODE_INFORMATION_NTF: nodeID: {}, order: {}, placement: {}, name: \"{}\", velocity: {}, nodeTypeSubType: {}, productGroup: {}, productType: {}, "
                        + "nodeVariation: {}, powerMode: {}, buildNumber: {}, serialNumber: {}, state: {}, currentPosition: {}, target: {}, fp1currentPosition: {}, "
                        + "fp2currentPosition: {}, fp3currentPosition: {}, fp4currentPosition: {}, remainingTime: {}, timeStamp: {}, nbrOfAlias: {}, aliasArray: {}",
                this.nodeID, this.order, this.placement, this.name, this.velocity, this.nodeTypeSubType,
                this.productGroup, this.productType, this.nodeVariation, this.powerMode, this.buildNumber,
                this.serialNumber, this.state, this.currentPosition, this.target, this.fp1currentPosition,
                this.fp2currentPosition, this.fp3currentPosition, this.fp4currentPosition, this.remainingTime,
                this.timeStamp, this.nbrOfAlias, this.aliasArray);

        // TODO : update properties => fire an event that thingHandler watches
        // updateProperty(PROPERTY_ASSIGNED_NAME, deviceInformationState.name); // method available in thingHandler

        // TODO : update position channel
    }

    public short getNodeID() {
        return this.nodeID;
    }

    public short getOrder() {
        return this.order;
    }
}
