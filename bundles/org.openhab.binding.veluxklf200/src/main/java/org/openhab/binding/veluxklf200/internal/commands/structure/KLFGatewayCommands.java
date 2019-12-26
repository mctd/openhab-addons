/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command constants as defined by the Velux KLF200 API documentation.
 *
 * @author MFK - Initial Contribution
 */
public enum KLFGatewayCommands {
    /** Login to the KLF200 with Password. */
    GW_PASSWORD_ENTER_REQ((short) 0x3000),

    /** Provides result of Login attempt. */
    GW_PASSWORD_ENTER_CFM((short) 0x3001),

    /** Get information about a specific node on the KLF200. */
    GW_GET_NODE_INFORMATION_REQ((short) 0x0200),

    /** Confirmation that a node request has been accepted. */
    GW_GET_NODE_INFORMATION_CFM((short) 0x0201),

    /** Notification of information about a node. */
    GW_GET_NODE_INFORMATION_NTF((short) 0x0210),

    /** Get information about all nodes on th KLF200. */
    GW_GET_ALL_NODES_INFORMATION_REQ((short) 0x0202),

    /** Confirmation that request for all nodes was recieved. */
    GW_GET_ALL_NODES_INFORMATION_CFM((short) 0x0203),

    /** Response with details of a single node. */
    GW_GET_ALL_NODES_INFORMATION_NTF((short) 0x0204),

    /** Indicates that all node information has been provided. */
    GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF((short) 0x0205),

    /** Get the summary state of the KLF200 bridge. */
    GW_GET_STATE_REQ((short) 0x000C),

    /** Response with summary state of KLF200. */
    GW_GET_STATE_CFM((short) 0x000D),

    /** Send command request. */
    GW_COMMAND_SEND_REQ((short) 0x0300),

    /** Send command confirmation. */
    GW_COMMAND_SEND_CFM((short) 0x0301),

    /** Notification when a command starts to execute. */
    GW_COMMAND_RUN_STATUS_NTF((short) 0x0302),

    /** Notification with detils of time remaining for command to execute fully. */
    GW_COMMAND_REMAINING_TIME_NTF((short) 0x0303),

    /** Indicates that a session has finished (eg: sequence or listing has completed). */
    GW_SESSION_FINISHED_NTF((short) 0x0304),

    /**
     * Indicates an error has occurred. Specifically, that the unit was unable
     * to understand the command sent and/or that the command was malformed
     */
    GW_ERROR_NTF((short) 0x0000),

    /** Get the list of scenes configured on the KLF200. */
    GW_GET_SCENE_LIST_REQ((short) 0x040C),

    /** Confirmation of get scenes command. */
    GW_GET_SCENE_LIST_CFM((short) 0x040D),

    /** Notification in response to getting scenes with the list of scenes included in the payload. */
    GW_GET_SCENE_LIST_NTF((short) 0x040E),

    /** Activate a scene. */
    GW_ACTIVATE_SCENE_REQ((short) 0x0412),

    /** Confirmation that a scene activation has been accepted. */
    GW_ACTIVATE_SCENE_CFM((short) 0x0413),

    /** Notification that a nodes state has changed. */
    GW_NODE_STATE_POSITION_CHANGED_NTF((short) 0x0211),

    /** Get Version information request. */
    GW_GET_VERSION_REQ((short) 0x0008),

    /** Get Version information response. */
    GW_GET_VERSION_CFM((short) 0x0009),

    /** Get Protocol information request. */
    GW_GET_PROTOCOL_VERSION_REQ((short) 0x000A),

    /** Get Protocol information response. */
    GW_GET_PROTOCOL_VERSION_CFM((short) 0x000B),

    /** Enable House Status Monitoring */
    GW_HOUSE_STATUS_MONITOR_ENABLE_REQ((short) 0x0240),

    /** Confirmation on enabling House Status Monitoring */
    GW_HOUSE_STATUS_MONITOR_ENABLE_CFM((short) 0x0241),

    /** Set the current time */
    GW_SET_UTC_REQ((short) 0x2000),

    /** Confirmation on setting the current time */
    GW_SET_UTC_CFM((short) 0x2001),

    /** Set the current timezone */
    GW_RTC_SET_TIME_ZONE_REQ((short) 0x2002),

    /** Confirmation on setting the current timezone */
    GW_RTC_SET_TIME_ZONE_CFM((short) 0x2003),

    /** Stop a scene from executing */
    GW_STOP_SCENE_REQ((short) 0x0415),

    /** Confirmation that a scene is stopping */
    GW_STOP_SCENE_CFM((short) 0x0416);

    final short commandCode;

    /** Logging. */
    private final static Logger logger = LoggerFactory.getLogger(KLFGatewayCommands.class);

    KLFGatewayCommands(short commandCode) {
        this.commandCode = commandCode;
    }

    /**
     * Get the code of the command.
     *
     * @return The code of the command as a short.
     */
    public short getCode() {
        return commandCode;
    }

    /**
     * Find the command matching the given code.
     *
     * @param commandCode Command code to look for.
     * @return the {@link KLFGatewayCommands} matching commandCode if found, null otherwise
     */
    public static KLFGatewayCommands fromCode(short commandCode) {
        for (KLFGatewayCommands value : values()) {
            short code = value.getCode();
            if (commandCode == code) {
                return value;
            }
        }
        logger.error("Unsupported command with code: {}", String.format("0x%04X", commandCode));
        return null;
    }
}