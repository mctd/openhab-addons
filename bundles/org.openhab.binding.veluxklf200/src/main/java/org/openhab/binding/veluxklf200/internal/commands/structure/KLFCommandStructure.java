/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.veluxklf200.internal.commands.structure;

/**
 * Enum representation of the logical grouping of commands that can be sent and
 * received from the KLF200 unit.
 *
 * @author emmanuel
 */
public enum KLFCommandStructure {
    //@formatter:off
    /**
     * The nothing. This is a dummy command to terminate the consumers. It is
     * never actually sent to the KLF200 unit.
     */
    TERMINATE(
            "Used to terminate the queue manager",
            "TERMINATE",
            null,
            new KLFGatewayCommands[] {},
            KLFCommandStructure.MASK_NO_REQS
            ),

    /** Login to the KLF. */
    LOGIN(
            "Login to the KLF200 Gateway using a password.",
            "GW_PASSWORD_ENTER_REQ",
            KLFGatewayCommands.GW_PASSWORD_ENTER_REQ,
            new KLFGatewayCommands[] { KLFGatewayCommands.GW_PASSWORD_ENTER_CFM },
            KLFCommandStructure.MASK_NO_REQS
            ),

    /**
     * Ping the KLF to get basic high-level information as well as keep the
     * socket connection alive.
     */
    PING(
            "Ping the gateway to get state information and keep the socket connection alive.",
            "GW_GET_STATE_REQ",
            KLFGatewayCommands.GW_GET_STATE_REQ,
            new KLFGatewayCommands[] { KLFGatewayCommands.GW_GET_STATE_CFM },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** The get all node information. */
    GET_ALL_NODE_INFORMATION(
            "Query all of the nodes setup / configured on the KLF200 device.",
            "GW_GET_ALL_NODES_INFORMATION_REQ",
            KLFGatewayCommands.GW_GET_ALL_NODES_INFORMATION_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_GET_ALL_NODES_INFORMATION_CFM,
                    KLFGatewayCommands.GW_GET_ALL_NODES_INFORMATION_NTF,
                    KLFGatewayCommands.GW_GET_ALL_NODES_INFORMATION_FINISHED_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** The individual node information. */
    GET_NODE_INFORMATION(
            "Query specific node on the KLF200 device.",
            "GW_GET_NODE_INFORMATION_REQ",
            KLFGatewayCommands.GW_GET_NODE_INFORMATION_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_GET_NODE_INFORMATION_CFM,
                    KLFGatewayCommands.GW_GET_NODE_INFORMATION_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_NODE_SPECIFIC
            ),

    /** Send a command to a Velux node. */
    SEND_NODE_COMMAND(
            "Send a command to a specific node on the KLF200 device.",
            "GW_COMMAND_SEND_REQ",
            KLFGatewayCommands.GW_COMMAND_SEND_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_COMMAND_SEND_CFM,
                    KLFGatewayCommands.GW_COMMAND_RUN_STATUS_NTF,
                    KLFGatewayCommands.GW_COMMAND_REMAINING_TIME_NTF,
                    KLFGatewayCommands.GW_SESSION_FINISHED_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_SESSION_REQD
            ),

    SET_NODE_VELOCITY(
            "Set a node velocity",
            "GW_SET_NODE_VELOCITY_REQ",
            KLFGatewayCommands.GW_SET_NODE_VELOCITY_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_SET_NODE_VELOCITY_CFM,
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_NODE_SPECIFIC
            ),

    SET_NODE_NAME(
            "Set a node name",
            "GW_SET_NODE_NAME_REQ",
            KLFGatewayCommands.GW_SET_NODE_NAME_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_SET_NODE_NAME_CFM,
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_NODE_SPECIFIC
            ),

    REBOOT(
            "Reboots the KLF200",
            "GW_REBOOT_REQ",
            KLFGatewayCommands.GW_REBOOT_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_REBOOT_CFM,
                },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** Get list of scenes configured on the KLF200. */
    GET_ALL_SCENES(
            "Get a list of all scenes configured on the KLF200 unit.",
            "GW_GET_SCENE_LIST_REQ",
            KLFGatewayCommands.GW_GET_SCENE_LIST_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_GET_SCENE_LIST_CFM,
                    KLFGatewayCommands.GW_GET_SCENE_LIST_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** Activate a scene. */
    ACTIVATE_SCENE(
            "Activate a specific scene on the KLF200 unit.",
            "GW_ACTIVATE_SCENE_REQ",
            KLFGatewayCommands.GW_ACTIVATE_SCENE_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_ACTIVATE_SCENE_CFM,
                    KLFGatewayCommands.GW_COMMAND_RUN_STATUS_NTF,
                    KLFGatewayCommands.GW_COMMAND_REMAINING_TIME_NTF,
                    KLFGatewayCommands.GW_SESSION_FINISHED_NTF,
                    KLFGatewayCommands.GW_NODE_STATE_POSITION_CHANGED_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_SESSION_REQD
            ),

    /** Stop a scene during execution. */
    STOP_SCENE(
            "Stop a specific scene on the KLF200 unit.",
            "GW_STOP_SCENE_REQ",
            KLFGatewayCommands.GW_STOP_SCENE_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_STOP_SCENE_CFM,
                    KLFGatewayCommands.GW_SESSION_FINISHED_NTF
                },
            KLFCommandStructure.MASK_AUTH_REQD | KLFCommandStructure.MASK_SESSION_REQD
            ),

    /** The get protocol. */
    GET_PROTOCOL(
            "Gets protocol version information from the KLF200 unit.",
            "GW_GET_PROTOCOL_VERSION_REQ",
            KLFGatewayCommands.GW_GET_PROTOCOL_VERSION_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_GET_PROTOCOL_VERSION_CFM
                },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** The get version. */
    GET_VERSION(
            "Gets software version information from the KLF200 unit.",
            "GW_GET_VERSION_REQ",
            KLFGatewayCommands.GW_GET_VERSION_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_GET_VERSION_CFM
                },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    /** Set Time */
    SET_TIME(
            "Sets the time on the KLF200",
            "GW_SET_UTC_REQ",
            KLFGatewayCommands.GW_SET_UTC_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_SET_UTC_CFM
            },
            KLFCommandStructure.MASK_AUTH_REQD
            ),

    ENABLE_HOUSE_STATUS_MONITOR(
            "Enables house status monitor on the KLF200",
            "GW_HOUSE_STATUS_MONITOR_ENABLE_REQ",
            KLFGatewayCommands.GW_HOUSE_STATUS_MONITOR_ENABLE_REQ,
            new KLFGatewayCommands[] {
                    KLFGatewayCommands.GW_HOUSE_STATUS_MONITOR_ENABLE_CFM
            },
            KLFCommandStructure.MASK_AUTH_REQD
            );


    /** Bit mask to indicate that commmand has no requirements */
    public static final int MASK_NO_REQS = 0b0000;

    /** Bit mask to indicate that a command requires a session */
    public static final int MASK_SESSION_REQD = 0b0010;

    /** Bit mask to indicate that a command requires authentication */
    public static final int MASK_AUTH_REQD = 0b0001;

    /** Bit mask to indicate that a command is node specific */
    public static final int MASK_NODE_SPECIFIC = 0b0100;

    /** Printable description of this command. */
    private String description;

    /**
     * Indicates additional configuration / attributes of the command. Int represents a bit mask of possible values. See
     * {@link MASK_SESSION_REQD}, {@link MASK_AUTH_REQD} and {@link MASK_NODE_SPECIFIC}
     */
    private int attributes;

    /** Command code for this command as per the KLF200 API specification. */
    private KLFGatewayCommands command;

    /**
     * List of responses (command codes) that may be received in response to
     * executing this command.
     */
    private KLFGatewayCommands[] handledResponses;

    /** Display code for this command. Typically used in log files. */
    private String displayCode;

    /**
     * Instantiates a new KLF command structure.
     *
     * @param description
     *            Printable description of this command.
     * @param displayCode
     *            Display code for this command. Typically used in log files.
     * @param commandCode
     *            Command code for this command as per the KLF200 API
     *            specification.
     * @param responseCodes
     *            List of responses (command codes) that may be received in
     *            response to executing this command.
     * @param authRequired
     *            Indicates whether or not authentication is required before
     *            this command can be executed.
     */
    private KLFCommandStructure(String description, String displayCode, KLFGatewayCommands command,
            KLFGatewayCommands[] handledResponses, int attributes) {

        this.description = description;
        this.displayCode = displayCode;
        this.command = command;
        this.handledResponses = handledResponses;
        this.attributes = attributes;
    }

    /**
     * Gets the display code.
     *
     * @return the display code
     */
    public String getDisplayCode() {
        return this.displayCode;
    }

    /**
     * Checks if is auth is required.
     *
     * @return true, if is auth is required
     */
    public boolean isAuthRequired() {
        return (this.attributes & MASK_AUTH_REQD) == MASK_AUTH_REQD ? true : false;
    }

    /**
     * Checks if a session parameter is required for this command.
     *
     * @return true, if is a session is required
     */
    public boolean isSessionRequired() {
        return (this.attributes & MASK_SESSION_REQD) == MASK_SESSION_REQD ? true : false;
    }

    /**
     * Checks attributes of the command to determine if the command itself is node specific. A node specific command
     * that does not use a session is one that has to be treated specially to make sure that individual node commands
     * executed in parallel do not interfere with one another.
     *
     * @return true, if is a session is required
     */
    public boolean isNodeSpecific() {
        return (this.attributes & MASK_NODE_SPECIFIC) == MASK_NODE_SPECIFIC ? true : false;
    }

    /**
     * Gets a printable description of this command.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the command code.
     *
     * @return the command code
     */
    public KLFGatewayCommands getCommand() {
        return command;
    }

    /**
     * Gets the response codes that this command is capable of handling.
     *
     * @return the response codes
     */
    public KLFGatewayCommands[] getHandledResponseCodes() {
        return handledResponses;
    }

    /**
     * Determines whether or not this command is capable of handling a given KLF
     * response code (command code).
     *
     * @param responseCode
     *            The response code (command code) to be checked
     * @return true, if this command can handle the specified responseCode,
     *         false otherwise.
     */
    public boolean canHandle(KLFGatewayCommands command) {
        for (KLFGatewayCommands c : handledResponses) {
            if (c == command) {
                return true;
            }
        }
        return false;
    }
}