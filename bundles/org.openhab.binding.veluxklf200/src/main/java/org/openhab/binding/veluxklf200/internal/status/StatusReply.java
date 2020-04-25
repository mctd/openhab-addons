/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.status;

/**
 * Contains current state of the node. (Error code).
 *
 * @author emmanuel
 */
public enum StatusReply {
    /** Used to indicate unknown reply. */
    UNKNOWN_STATUS_REPLY((byte) 0x00, "Used to indicate unknown reply"),
    /** Indicates no errors detected. */
    COMMAND_COMPLETED_OK((byte) 0x01, "Indicates no errors detected"),
    /** Indicates no communication to node. */
    NO_CONTACT((byte) 0x02, "Indicates no communication to node"),
    /** Indicates manually operated by a user. */
    MANUALLY_OPERATED((byte) 0x03, "Indicates manually operated by a user"),
    /** Indicates node has been blocked by an object. */
    BLOCKED((byte) 0x04, "Indicates node has been blocked by an object"),
    /** Indicates the node contains a wrong system key. */
    WRONG_SYSTEMKEY((byte) 0x05, "Indicates the node contains a wrong system key"),
    /** Indicates the node is locked on this priority level. */
    PRIORITY_LEVEL_LOCKED((byte) 0x06, "Indicates the node is locked on this priority level"),
    /** Indicates node has stopped in another position than expected. */
    REACHED_WRONG_POSITION((byte) 0x07, "Indicates node has stopped in another position than expected"),
    /** Indicates an error has occurred during execution of command. */
    ERROR_DURING_EXECUTION((byte) 0x08, "Indicates an error has occurred during execution of command"),
    /** Indicates no movement of the node parameter. */
    NO_EXECUTION((byte) 0x09, "Indicates no movement of the node parameter"),
    /** Indicates the node is calibrating the parameters. */
    CALIBRATING((byte) 0x0A, "Indicates the node is calibrating the parameters"),
    /** Indicates the node power consumption is too high. */
    POWER_CONSUMPTION_TOO_HIGH((byte) 0x0B, "Indicates the node power consumption is too high"),
    /** Indicates the node power consumption is too low. */
    POWER_CONSUMPTION_TOO_LOW((byte) 0x0C, "Indicates the node power consumption is too low"),
    /** Indicates door lock errors. (Door open during lock command) */
    LOCK_POSITION_OPEN((byte) 0x0D, "Indicates door lock errors. (Door open during lock command)"),
    /** Indicates the target was not reached in time. */
    MOTION_TIME_TOO_LONG_COMMUNICATION_ENDED((byte) 0x0E, "Indicates the target was not reached in time"),
    /** Indicates the node has gone into thermal protection mode. */
    THERMAL_PROTECTION((byte) 0x0F, "Indicates the node has gone into thermal protection mode"),
    /** Indicates the node is not currently operational. */
    PRODUCT_NOT_OPERATIONAL((byte) 0x10, "Indicates the node is not currently operational"),
    /** Indicates the filter needs maintenance. */
    FILTER_MAINTENANCE_NEEDED((byte) 0x11, "Indicates the filter needs maintenance"),
    /** Indicates the battery level is low. */
    BATTERY_LEVEL((byte) 0x12, "Indicates the battery level is low"),
    /** Indicates the node has modified the target value of the command. */
    TARGET_MODIFIED((byte) 0x13, "Indicates the node has modified the target value of the command"),
    /** Indicates this node does not support the mode received. */
    MODE_NOT_IMPLEMENTED((byte) 0x14, "Indicates this node does not support the mode received"),
    /** Indicates the node is unable to move in the right direction. */
    COMMAND_INCOMPATIBLE_TO_MOVEMENT((byte) 0x15, "Indicates the node is unable to move in the right direction"),
    /** Indicates dead bolt is manually locked during unlock command. */
    USER_ACTION((byte) 0x16, "Indicates dead bolt is manually locked during unlock command"),
    /** Indicates dead bolt error. */
    DEAD_BOLT_ERROR((byte) 0x17, "Indicates dead bolt error"),
    /** Indicates the node has gone into automatic cycle mode. */
    AUTOMATIC_CYCLE_ENGAGED((byte) 0x18, "Indicates the node has gone into automatic cycle mode"),
    /** Indicates wrong load on node. */
    WRONG_LOAD_CONNECTED((byte) 0x19, "Indicates wrong load on node"),
    /** Indicates that node is unable to reach received colour code. */
    COLOUR_NOT_REACHABLE((byte) 0x1A, "Indicates that node is unable to reach received colour code"),
    /** Indicates the node is unable to reach received target position. */
    TARGET_NOT_REACHABLE((byte) 0x1B, "Indicates the node is unable to reach received target position"),
    /** Indicates io-protocol has received an invalid index. */
    BAD_INDEX_RECEIVED((byte) 0x1C, "Indicates io-protocol has received an invalid index"),
    /** Indicates that the command was overruled by a new command. */
    COMMAND_OVERRULED((byte) 0x1D, "Indicates that the command was overruled by a new command"),
    /** Indicates that the node reported waiting for power. */
    NODE_WAITING_FOR_POWER((byte) 0x1E, "Indicates that the node reported waiting for power"),
    /** Indicates an unknown error code received. (Hex code is shown on display) */
    INFORMATION_CODE((byte) 0xDF, "Indicates an unknown error code received. (Hex code is shown on display)"),
    /** Indicates the parameter was limited by an unknown device. (Same as LIMITATION_BY_UNKNOWN_DEVICE) */
    PARAMETER_LIMITED((byte) 0xE0,
            "Indicates the parameter was limited by an unknown device. (Same as LIMITATION_BY_UNKNOWN_DEVICE)"),
    /** Indicates the parameter was limited by local button. */
    LIMITATION_BY_LOCAL_USER((byte) 0xE1, "Indicates the parameter was limited by local button"),
    /** Indicates the parameter was limited by a remote control. */
    LIMITATION_BY_USER((byte) 0xE2, "Indicates the parameter was limited by a remote control"),
    /** Indicates the parameter was limited by a rain sensor. */
    LIMITATION_BY_RAIN((byte) 0xE3, "Indicates the parameter was limited by a rain sensor"),
    /** Indicates the parameter was limited by a timer. */
    LIMITATION_BY_TIMER((byte) 0xE4, "Indicates the parameter was limited by a timer"),
    /** Indicates the parameter was limited by a power supply. */
    LIMITATION_BY_UPS((byte) 0xE6, "Indicates the parameter was limited by a power supply"),
    /** Indicates the parameter was limited by an unknown device. (Same as PARAMETER_LIMITED) */
    LIMITATION_BY_UNKNOWN_DEVICE((byte) 0xE7,
            "Indicates the parameter was limited by an unknown device. (Same as PARAMETER_LIMITED)"),
    /** Indicates the parameter was limited by a standalone automatic controller. */
    LIMITATION_BY_SAAC((byte) 0xEA, "Indicates the parameter was limited by a standalone automatic controller"),
    /** Indicates the parameter was limited by a wind sensor. */
    LIMITATION_BY_WIND((byte) 0xEB, "Indicates the parameter was limited by a wind sensor"),
    /** Indicates the parameter was limited by the node itself. */
    LIMITATION_BY_MYSELF((byte) 0xEC, "Indicates the parameter was limited by the node itself"),
    /** Indicates the parameter was limited by an automatic cycle. */
    LIMITATION_BY_AUTOMATIC_CYCLE((byte) 0xED, "Indicates the parameter was limited by an automatic cycle"),
    /** Indicates the parameter was limited by an emergency. */
    LIMITATION_BY_EMERGENCY((byte) 0xEE, "Indicates the parameter was limited by an emergency");

    /** Status value. */
    private byte value;
    private String description;

    /**
     * Instantiates a new StatusReply.
     *
     * @param value StatusReply value
     */
    private StatusReply(byte value, String description) {
        this.value = value;
    }

    /**
     * Gets the status value.
     *
     * @return Value of StatusReply
     */
    public byte getValue() {
        return this.value;
    }

    /**
     * Creates a StatusReply from its value.
     *
     * @param value Value of StatusReply
     * @return StatusReply matching the value.
     */
    public static StatusReply fromCode(byte code) {
        for (StatusReply testStatus : StatusReply.values()) {
            if (testStatus.getValue() == code) {
                return testStatus;
            }
        }

        // TODO : log warn unmapped value
        return UNKNOWN_STATUS_REPLY;
    }

    @Override
    public String toString() {
        return this.description;
    }

}