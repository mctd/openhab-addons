/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains current state of the node. (Error code).
 *
 * @author emmanuel
 */
@NonNullByDefault
public enum StatusReply {
    /** Used to indicate unknown reply. */
    UNKNOWN_STATUS_REPLY((byte) 0x00, "Unknown reply"),
    /** Indicates no errors detected. */
    COMMAND_COMPLETED_OK((byte) 0x01, "No errors detected"),
    /** Indicates no communication to node. */
    NO_CONTACT((byte) 0x02, "No communication to node"),
    /** Indicates manually operated by a user. */
    MANUALLY_OPERATED((byte) 0x03, "Manually operated by a user"),
    /** Indicates node has been blocked by an object. */
    BLOCKED((byte) 0x04, "Node has been blocked by an object"),
    /** Indicates the node contains a wrong system key. */
    WRONG_SYSTEMKEY((byte) 0x05, "Node contains a wrong system key"),
    /** Indicates the node is locked on this priority level. */
    PRIORITY_LEVEL_LOCKED((byte) 0x06, "Node is locked on this priority level"),
    /** Indicates node has stopped in another position than expected. */
    REACHED_WRONG_POSITION((byte) 0x07, "Node has stopped in another position than expected"),
    /** Indicates an error has occurred during execution of command. */
    ERROR_DURING_EXECUTION((byte) 0x08, "An error has occurred during execution of command"),
    /** Indicates no movement of the node parameter. */
    NO_EXECUTION((byte) 0x09, "No movement of the node parameter"),
    /** Indicates the node is calibrating the parameters. */
    CALIBRATING((byte) 0x0A, "Node is calibrating the parameters"),
    /** Indicates the node power consumption is too high. */
    POWER_CONSUMPTION_TOO_HIGH((byte) 0x0B, "Node power consumption is too high"),
    /** Indicates the node power consumption is too low. */
    POWER_CONSUMPTION_TOO_LOW((byte) 0x0C, "Node power consumption is too low"),
    /** Indicates door lock errors. (Door open during lock command) */
    LOCK_POSITION_OPEN((byte) 0x0D, "Door lock errors. (Door open during lock command)"),
    /** Indicates the target was not reached in time. */
    MOTION_TIME_TOO_LONG_COMMUNICATION_ENDED((byte) 0x0E, "The target was not reached in time"),
    /** Indicates the node has gone into thermal protection mode. */
    THERMAL_PROTECTION((byte) 0x0F, "Node has gone into thermal protection mode"),
    /** Indicates the node is not currently operational. */
    PRODUCT_NOT_OPERATIONAL((byte) 0x10, "Node is not currently operational"),
    /** Indicates the filter needs maintenance. */
    FILTER_MAINTENANCE_NEEDED((byte) 0x11, "Filter needs maintenance"),
    /** Indicates the battery level is low. */
    BATTERY_LEVEL((byte) 0x12, "Battery level is low"),
    /** Indicates the node has modified the target value of the command. */
    TARGET_MODIFIED((byte) 0x13, "Node has modified the target value of the command"),
    /** Indicates this node does not support the mode received. */
    MODE_NOT_IMPLEMENTED((byte) 0x14, "This node does not support the mode received"),
    /** Indicates the node is unable to move in the right direction. */
    COMMAND_INCOMPATIBLE_TO_MOVEMENT((byte) 0x15, "The node is unable to move in the right direction"),
    /** Indicates dead bolt is manually locked during unlock command. */
    USER_ACTION((byte) 0x16, "Dead bolt is manually locked during unlock command"),
    /** Indicates dead bolt error. */
    DEAD_BOLT_ERROR((byte) 0x17, "Dead bolt error"),
    /** Indicates the node has gone into automatic cycle mode. */
    AUTOMATIC_CYCLE_ENGAGED((byte) 0x18, "Node has gone into automatic cycle mode"),
    /** Indicates wrong load on node. */
    WRONG_LOAD_CONNECTED((byte) 0x19, "Wrong load on node"),
    /** Indicates that node is unable to reach received colour code. */
    COLOUR_NOT_REACHABLE((byte) 0x1A, "Node is unable to reach received colour code"),
    /** Indicates the node is unable to reach received target position. */
    TARGET_NOT_REACHABLE((byte) 0x1B, "Node is unable to reach received target position"),
    /** Indicates io-protocol has received an invalid index. */
    BAD_INDEX_RECEIVED((byte) 0x1C, "io-protocol has received an invalid index"),
    /** Indicates that the command was overruled by a new command. */
    COMMAND_OVERRULED((byte) 0x1D, "Command overruled by a new command"),
    /** Indicates that the node reported waiting for power. */
    NODE_WAITING_FOR_POWER((byte) 0x1E, "Node reported waiting for power"),
    /** Indicates an unknown error code received. (Hex code is shown on display) */
    INFORMATION_CODE((byte) 0xDF, "Unknown error code received. (Hex code is shown on display)"),
    /** Indicates the parameter was limited by an unknown device. (Same as LIMITATION_BY_UNKNOWN_DEVICE) */
    PARAMETER_LIMITED((byte) 0xE0,
            "Parameter was limited by an unknown device. (Same as LIMITATION_BY_UNKNOWN_DEVICE)"),
    /** Indicates the parameter was limited by local button. */
    LIMITATION_BY_LOCAL_USER((byte) 0xE1, "Parameter was limited by local button"),
    /** Indicates the parameter was limited by a remote control. */
    LIMITATION_BY_USER((byte) 0xE2, "Parameter was limited by a remote control"),
    /** Indicates the parameter was limited by a rain sensor. */
    LIMITATION_BY_RAIN((byte) 0xE3, "Parameter was limited by a rain sensor"),
    /** Indicates the parameter was limited by a timer. */
    LIMITATION_BY_TIMER((byte) 0xE4, "Parameter was limited by a timer"),
    /** Indicates the parameter was limited by a power supply. */
    LIMITATION_BY_UPS((byte) 0xE6, "Parameter was limited by a power supply"),
    /** Indicates the parameter was limited by an unknown device. (Same as PARAMETER_LIMITED) */
    LIMITATION_BY_UNKNOWN_DEVICE((byte) 0xE7,
            "Parameter was limited by an unknown device. (Same as PARAMETER_LIMITED)"),
    /** Indicates the parameter was limited by a standalone automatic controller. */
    LIMITATION_BY_SAAC((byte) 0xEA, "Parameter was limited by a standalone automatic controller"),
    /** Indicates the parameter was limited by a wind sensor. */
    LIMITATION_BY_WIND((byte) 0xEB, "Parameter was limited by a wind sensor"),
    /** Indicates the parameter was limited by the node itself. */
    LIMITATION_BY_MYSELF((byte) 0xEC, "Parameter was limited by the node itself"),
    /** Indicates the parameter was limited by an automatic cycle. */
    LIMITATION_BY_AUTOMATIC_CYCLE((byte) 0xED, "Parameter was limited by an automatic cycle"),
    /** Indicates the parameter was limited by an emergency. */
    LIMITATION_BY_EMERGENCY((byte) 0xEE, "Parameter was limited by an emergency");

    private static final Logger logger = LoggerFactory.getLogger(StatusReply.class);
    private byte code;
    private String description;

    private StatusReply(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    /**
     * Creates a StatusReply from its value.
     *
     * @param code Code of StatusReply
     * @return StatusReply matching the code.
     */
    public static StatusReply fromCode(byte code) {
        for (StatusReply testStatus : StatusReply.values()) {
            if (testStatus.getCode() == code) {
                return testStatus;
            }
        }

        logger.error("Invalid code: {}", code);
        return UNKNOWN_STATUS_REPLY;
    }

    @Override
    public String toString() {
        return this.description;
    }

}