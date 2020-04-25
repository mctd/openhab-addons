/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhab.binding.veluxklf200.internal.status.Velocity;

/**
 * Representation of a Velux node.
 *
 * @author emmanuel
 */
public class VeluxNode {

    /** The node id. */
    private byte nodeId;

    /**
     * The order in which the node occurs in the system (as defined on the KLF200).
     */
    private short order;

    /**
     * Indicates the placement of the node as defined by the KLF200. Placement can be used to store a room group index
     * or house group index number.
     */
    private byte placement;

    /** The name of the node. */
    private String name;

    /**
     * Indicates the velocity the node is operating at (eg: Default, Silent, etc..).
     */
    private Velocity velocity;

    /** The type of node. */
    private VeluxNodeType nodeTypeSubType;

    /** The product group. */
    private byte productGroup;

    /** The product type. */
    private byte productType;

    // NodeVariation byte ?

    /** The power mode. */
    private VeluxPowerMode powerMode;

    /** The build number. */
    private byte buildNumber;

    /** The serial number. */
    private String serialNumber;

    /** The state. */
    private VeluxState state;

    /** The current position. */
    private VeluxPosition currentPosition;

    /** The target position. */
    private VeluxPosition targetPosition;

    /** The FP 1 current position. */
    private VeluxPosition fP1currentPosition;

    /** The FP 2 current position. */
    private VeluxPosition fP2currentPosition;

    /** The FP 3 current position. */
    private VeluxPosition fP3currentPosition;

    /** The FP 4 current position. */
    private VeluxPosition fP4currentPosition;

    /** The remaining time. */
    private int remainingTime;

    /** The last command. */
    private Date lastCommand;

    /** The node variation. */
    private VeluxNodeVariation nodeVariation;

    /**
     * Instantiates a new velux node.
     */
    private VeluxNode() {
    }

    /**
     * Instantiates a new velux node.
     *
     * @param nodeId
     *            the node id
     * @param order
     *            the order
     * @param placement
     *            the placement
     * @param name
     *            the name
     * @param veluxVelocity
     *            the velux velocity
     * @param nodeTypeSubType
     *            the node type
     * @param productGroup
     *            the product group
     * @param productType
     *            the product type
     * @param nodeVariation
     *            the node variation
     * @param powerMode
     *            the power mode
     * @param buildNumber
     *            the build number
     * @param serialNumber
     *            the serial number
     * @param state
     *            the state
     * @param currentPosition
     *            the current position
     * @param targetPosition
     *            the target position
     * @param FP1currentPosition
     *            the FP 1 current position
     * @param FP2currentPosition
     *            the FP 2 current position
     * @param FP3currentPosition
     *            the FP 3 current position
     * @param FP4currentPosition
     *            the FP 4 current position
     * @param remainingTime
     *            the remaining time
     * @param lastCommand
     *            the last command
     */
    public VeluxNode(byte nodeId, short order, byte placement, String name, byte veluxVelocity, short nodeTypeSubType,
            byte productGroup, byte productType, byte nodeVariation, byte powerMode, byte buildNumber,
            String serialNumber, byte state, short currentPosition, short targetPosition, short FP1currentPosition,
            short FP2currentPosition, short FP3currentPosition, short FP4currentPosition, short remainingTime,
            long lastCommand) {
        this();
        this.nodeId = nodeId;
        this.order = order;
        this.placement = placement;
        this.name = name;
        this.velocity = Velocity.fromCode(veluxVelocity);
        this.nodeTypeSubType = VeluxNodeType.createFromCode(nodeTypeSubType);
        this.productGroup = productGroup;
        this.productType = productType;
        this.nodeVariation = VeluxNodeVariation.createFromCode(nodeVariation);
        this.powerMode = VeluxPowerMode.createFromCode(powerMode);
        this.buildNumber = buildNumber;
        this.serialNumber = serialNumber;
        this.state = VeluxState.createFromCode(state);
        this.currentPosition = VeluxPosition.createFromCode(currentPosition);
        this.targetPosition = VeluxPosition.createFromCode(targetPosition);
        this.fP1currentPosition = VeluxPosition.createFromCode(FP1currentPosition);
        this.fP2currentPosition = VeluxPosition.createFromCode(FP2currentPosition);
        this.fP3currentPosition = VeluxPosition.createFromCode(FP3currentPosition);
        this.fP4currentPosition = VeluxPosition.createFromCode(FP4currentPosition);
        this.remainingTime = remainingTime;
        this.lastCommand = new java.util.Date(lastCommand * 1000);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String ret = "";
        ret += "\nNode ID: " + nodeId;
        ret += "\nOrder: " + order;
        ret += "\nPlacement: " + placement;
        ret += "\nName: " + name;
        ret += "\nVelocity: " + velocity.getDisplayName();
        ret += "\nNode Type: " + nodeTypeSubType.getDisplayName();
        ret += "\nCurrent Position: " + currentPosition;
        ret += "\nTarget Position: " + targetPosition;
        ret += "\nFP1 Current Position: " + fP1currentPosition;
        ret += "\nFP2 Current Position: " + fP2currentPosition;
        ret += "\nFP3 Current Position: " + fP3currentPosition;
        ret += "\nFP4 Current Position: " + fP4currentPosition;
        ret += "\nRemaining Time: " + remainingTime;
        ret += "\nLast Command: " + (new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z").format(lastCommand));
        ret += "\nProduct Group: " + productGroup;
        ret += "\nProduct Type: " + productType;
        ret += "\nNode Variation: " + nodeVariation;
        ret += "\nPower Mode: " + powerMode;
        ret += "\nState: " + state;
        ret += "\nSerial Number: " + serialNumber;
        ret += "\n--------------------------------------------------\n";
        return ret;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Gets the order.
     *
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * Gets the placement.
     *
     * @return the placement
     */
    public int getPlacement() {
        return placement;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the velocity.
     *
     * @return the velocity
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Gets the node type sub type.
     *
     * @return the node type
     */
    public VeluxNodeType getNodeTypeSubType() {
        return nodeTypeSubType;
    }

    /**
     * Gets the serial number.
     *
     * @return the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Gets the builds the number.
     *
     * @return the builds the number
     */
    public int getBuildNumber() {
        return buildNumber;
    }

    /**
     * Gets the current position.
     *
     * @return the current position
     */
    public VeluxPosition getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets the target position.
     *
     * @return the target position
     */
    public VeluxPosition getTargetPosition() {
        return targetPosition;
    }

    /**
     * Gets the FP 1 current position.
     *
     * @return the FP 1 current position
     */
    public VeluxPosition getFP1currentPosition() {
        return fP1currentPosition;
    }

    /**
     * Gets the FP 2 current position.
     *
     * @return the FP 2 current position
     */
    public VeluxPosition getFP2currentPosition() {
        return fP2currentPosition;
    }

    /**
     * Gets the FP 3 current position.
     *
     * @return the FP 3 current position
     */
    public VeluxPosition getFP3currentPosition() {
        return fP3currentPosition;
    }

    /**
     * Gets the FP 4 current position.
     *
     * @return the FP 4 current position
     */
    public VeluxPosition getFP4currentPosition() {
        return fP4currentPosition;
    }

    /**
     * Gets the remaining time.
     *
     * @return the remaining time
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Gets the last command.
     *
     * @return the last command
     */
    public Date getLastCommand() {
        return lastCommand;
    }

    /**
     * Gets the product group.
     *
     * @return the product group
     */
    public int getProductGroup() {
        return productGroup;
    }

    /**
     * Gets the product type.
     *
     * @return the product type
     */
    public int getProductType() {
        return productType;
    }

    /**
     * Gets the node variation.
     *
     * @return the node variation
     */
    public VeluxNodeVariation getNodeVariation() {
        return nodeVariation;
    }

    /**
     * Gets the power mode.
     *
     * @return the power mode
     */
    public VeluxPowerMode getPowerMode() {
        return powerMode;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public VeluxState getState() {
        return state;
    }
}