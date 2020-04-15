/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.veluxklf200.internal.VeluxKLF200V2BindingConstants;
import org.openhab.binding.veluxklf200.internal.VeluxKLF200V2Configuration;
import org.openhab.binding.veluxklf200.internal.commands.CommandStatus;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdEnableHomeStatusMonitor;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetNodeInformation;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetProtocol;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetVersion;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdPing;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdSetTime;
import org.openhab.binding.veluxklf200.internal.components.VeluxPosition;
import org.openhab.binding.veluxklf200.internal.components.VeluxState;
import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.engine.KLFEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge for managing the connection with the Velux KLF200 unit.
 *
 * @author MFK - Initial Contribution
 */
public class KLF200BridgeHandler extends BaseBridgeHandler implements KLFEventListener {

    /** The logger. */
    private Logger logger = LoggerFactory.getLogger(KLF200BridgeHandler.class);

    /** Reference to the CommandProcessor that is setup and initialized at startup */
    private KLFCommandProcessor klf200;

    /** Keep record of the last known state of KLF200 in order to update Thing status only when there is a change */
    private ThingStatus lastKnownState = ThingStatus.UNKNOWN;

    private ScheduledFuture<?> refreshKnownDeviceSchedule;

    /**
     * Constructor
     *
     * @param bridge the bridge
     */
    public KLF200BridgeHandler(Bridge bridge) {
        super(bridge);
    }

    /*
     * Handle a command.
     *
     * @see
     * org.eclipse.smarthome.core.thing.binding.ThingHandler#handleCommand(org.eclipse.smarthome.core.thing.ChannelUID,
     * org.eclipse.smarthome.core.types.Command)
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling bridge command: {} for channel: {}", command, channelUID);
    }

    /*
     * Key initialization tasks are the establishment of a connection to the KLF200 based on the configuration
     * parameters (host, port and password). Assuming that these parameters have been set correctly, the main cause of a
     * failure in respect of connectivity is that the KLF200 has shutdown is TCP port. According to the API
     * documentation, this can happen after a period of inactivity. As such, if a connection failure occurs, it is
     * recommenced to reboot your KLF200 unit and retry. Once a connection has been established, the {@link
     * KLFCommandProcessor} takes care of sending periodic keep-alive pings to the unit to make sure that the socket
     * doesn't close again while the binding is running.
     *
     * Once the binding is running, every 5 minutes (based on default settings), the KLF200 is queried and the state of
     * all items that have been setup in OH is updated. In theory this should not be necessary as the binding takes
     * care of _CFM (confirmation) notifications from the KLF200 when things change. However, this is done just in case
     * something gets out of sync.
     *
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandler#initialize()
     */
    @Override
    public void initialize() {
        logger.debug("Initializing the KLF200 command processor.");
        VeluxKLF200V2Configuration config = getConfigAs(VeluxKLF200V2Configuration.class);
        String err = validateConfiguration(config);
        if (err != null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, err);
            return;
        }

        this.klf200 = new KLFCommandProcessor(this, config.hostname, config.port, config.password);
        // Register the handler as an event listener
        klf200.registerEventListener(this);
        klf200.initialize();
    }

    @Override
    public void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        logger.trace("Bridge updateStatus({}, {}, {})", status, statusDetail, description);
        VeluxKLF200V2Configuration config = getConfigAs(VeluxKLF200V2Configuration.class);

        // Don't update status if it didn't change
        if (status != lastKnownState) {
            super.updateStatus(status, statusDetail, description);

            if (status == ThingStatus.ONLINE) {
                logger.trace("Scheduling a periodic refresh for all devices.");
                // Periodically refresh the state of all of the nodes / things that we are interested in.
                refreshKnownDeviceSchedule = scheduler.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        refreshKnownDevices();
                    }
                }, 0, config.refresh, TimeUnit.MINUTES);
                // }, 0, 30, TimeUnit.SECONDS);
                // }, config.refresh, config.refresh, TimeUnit.MINUTES);
            } else {
                logger.trace("Cancelling scheduled periodic refresh for all devices.");
                // cancel schedule
                if (refreshKnownDeviceSchedule != null) {
                    refreshKnownDeviceSchedule.cancel(false);
                    refreshKnownDeviceSchedule = null;
                }
            }
        }
        lastKnownState = status;
    }

    /**
     * Periodic refresh of the status of each item that has been configured in OH.
     * See {@link initialize()} comments for further information.
     */
    private void refreshKnownDevices() {
        logger.debug("Refreshing all KLF200 devices");
        if (klf200 != null && klf200.isUpAndRunning()) {
            List<Thing> things = getThing().getThings();
            for (Thing t : things) {
                Channel channel = t.getChannel(VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID);

                if (channel != null) {
                    // TODO: handle every thing types. Idea : scan all things with channels of type "position" ?
                    // Refresh all roller shutter
                    if (VeluxKLF200V2BindingConstants.THING_TYPE_VELUX_ROLLER_SHUTTER.equals(t.getThingTypeUID())) {
                        logger.debug("Refreshing {} with Id {}", t, t.getUID().getId());
                        KlfCmdGetNodeInformation getNodeInfoCmd = new KlfCmdGetNodeInformation(
                                (byte) Integer.valueOf(t.getUID().getId()).intValue());
                        this.klf200.executeCommand(getNodeInfoCmd);
                        if (getNodeInfoCmd.getStatus() == CommandStatus.COMPLETE) {
                            Integer pctClosed = getNodeInfoCmd.getNode().getCurrentPosition().getPosition();
                            org.eclipse.smarthome.core.types.State itemState = UnDefType.UNDEF;
                            if (pctClosed == null) {
                                logger.debug(
                                        "Node '{}' position is currently unknown. Need to wait for an activation for KLF200 to learn its position.",
                                        getNodeInfoCmd.getNode().getName());
                            } else {
                                logger.debug("Node '{}' is currently {}% closed.", getNodeInfoCmd.getNode().getName(),
                                        pctClosed);
                                itemState = new PercentType(pctClosed);
                            }
                            updateState(channel.getUID(), itemState);
                        } else {
                            logger.error("Failed to retrieve information about node {}, error detail: {}",
                                    getNodeInfoCmd.getNodeId(), getNodeInfoCmd.getStatus().getErrorDetail());
                        }
                    }
                } else {
                    logger.error("Did not find a channel '{}' for Thing {}",
                            VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID, t.getUID());
                }
            }

            // Also update the time in case the unit has been rebooted since the binding was loaded
            klf200.executeCommand(new KlfCmdSetTime());
        } else {
            logger.warn("KLF200 is down, can't refresh devices.");
        }
    }

    /**
     * Once connected to the KLF200 unit, its internal properties such as Hardware, software and protocol versions are
     * retrieved and updated in the properties of the bridge. This is for informational purposes only.
     */
    public void updateBridgeProperties() {
        logger.debug("Updating Bridge properties");
        if (klf200 != null && klf200.isUpAndRunning()) {

            KlfCmdGetVersion ver = new KlfCmdGetVersion();
            Map<String, String> properties = this.editProperties();
            this.klf200.executeCommand(ver);
            if (ver.getStatus() == CommandStatus.COMPLETE) {
                properties.put("Hardware Version", ver.getHardwareVersion());
                properties.put("Software Version", ver.getSoftwareVersion());
                properties.put("Product Group", ver.getProductGroup());
                properties.put("Product Type", ver.getProductType());
            } else {
                logger.error("Unable to retrieve KLF20 Version Information: {}", ver.getStatus().getErrorDetail());
            }

            KlfCmdGetProtocol proto = new KlfCmdGetProtocol();
            this.klf200.executeCommand(proto);
            if (proto.getStatus() == CommandStatus.COMPLETE) {
                properties.put("Protocol", proto.getProtocol());
            } else {
                logger.error("Unable to retrieve KLF20 Protocol Information: {}", proto.getStatus().getErrorDetail());
            }

            KlfCmdPing ping = new KlfCmdPing();
            this.klf200.executeCommand(ping);
            if (ping.getStatus() == CommandStatus.COMPLETE) {
                properties.put("State", ping.getGatewayState());
            } else {
                logger.error("Unable to ping the KLF200: {}", ping.getStatus().getErrorDetail());
            }
            this.updateProperties(properties);

            KlfCmdSetTime time = new KlfCmdSetTime();
            this.klf200.executeCommand(time);
            if (time.getStatus() == CommandStatus.COMPLETE) {
                logger.debug("Time on the KLF200 updated to reflect current time on this system.");
            } else {
                logger.warn("Unable to update the time on the KLF200: {}", time.getStatus().getErrorDetail());
            }

            KlfCmdEnableHomeStatusMonitor monitor = new KlfCmdEnableHomeStatusMonitor();
            this.klf200.executeCommand(monitor);
            if (monitor.getStatus() == CommandStatus.COMPLETE) {
                logger.debug("Home status monitoring enabled.");
            } else {
                logger.warn("Unable to enable the home status monitor on the KLF200: {}",
                        monitor.getStatus().getErrorDetail());
            }
        } else {
            logger.warn("KLF200 is down, can't update bridge properties.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.smarthome.core.thing.binding.BaseThingHandler#dispose()
     */
    @Override
    public void dispose() {
        logger.debug("Disposing of bridge handler.");

        if (refreshKnownDeviceSchedule != null) {
            refreshKnownDeviceSchedule.cancel(false);
            refreshKnownDeviceSchedule = null;
        }

        this.klf200.shutdown();
    }

    /**
     * Validate configuration provided and in the event of issues, provide some feedback to aid the user.
     *
     * @param config config object
     * @return null in the case of validation passing, non-null if something was determined to be invalid.
     */
    private String validateConfiguration(VeluxKLF200V2Configuration config) {
        if (config.password == null) {
            return "A password must be specified. Please update the panel 'thing' configuration.";
        }
        if (config.port == null) {
            return "A port must be specified. Please update the panel 'thing' configuration.";
        }
        if (config.hostname == null) {
            return "An IP address or hostname must be specified. Please update the panel 'thing' configuration.";
        }
        return null;
    }

    /**
     * Handles a notification from the KLF200 in relative to a node.
     * These notifications are dispatched when the KLF200 broadcasts a _CFM (Confirmation).
     *
     * @param nodeId Node ID corresponds to the ID of the particular thing in OH
     * @param currentPosition Current position of the node. This needs to be interpreted to derive an actual position,
     *            see {@link VeluxPosition} in the case of a blind.
     */
    @Override
    public void handleEvent(byte nodeId, VeluxState state, VeluxPosition currentPosition, VeluxPosition targetPosition,
            VeluxPosition fp1CurrentPosition, VeluxPosition fp2CurrentPosition, VeluxPosition fp3CurrentPosition,
            VeluxPosition fp4CurrentPosition, int timeRemaining, long timestamp) {
        // Only process "DONE" events (TODO: should not as moving things is reported in non-DONE events)
        Thing thing = findThing(nodeId);
        if (thing != null) {
            Channel positionChannel = thing.getChannel(VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID);
            if (positionChannel == null) {
                logger.warn("Channel '{}' not found for thing '{}'",
                        VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID, thing.getLabel());
            }

            Channel movingChannel = thing.getChannel(VeluxKLF200V2BindingConstants.VELUX_MOVING_STATE_CHANNEL_ID);
            if (movingChannel == null) {
                logger.warn("Channel '{}' not found for thing '{}'",
                        VeluxKLF200V2BindingConstants.VELUX_MOVING_STATE_CHANNEL_ID, thing.getLabel());
            }

            Channel lastMovementChannel = thing
                    .getChannel(VeluxKLF200V2BindingConstants.VELUX_LAST_MOVEMENT_CHANNEL_ID);
            if (lastMovementChannel == null) {
                logger.warn("Channel '{}' not found for thing '{}'",
                        VeluxKLF200V2BindingConstants.VELUX_LAST_MOVEMENT_CHANNEL_ID, thing.getLabel());
            }

            if (state == VeluxState.NOT_USED) {
                // do nothing
            } else if (state == VeluxState.EXECUTING) {
                // Setting moving channel to true
                if (movingChannel != null) {
                    logger.debug("Setting '{}' moving state to {}", thing.getLabel(), OnOffType.ON);
                    updateState(movingChannel.getUID(), OnOffType.ON);
                }

                if (lastMovementChannel != null) {
                    // Setting last movement timestamp to received timestamp
                    DateTimeType now = new DateTimeType(ZonedDateTime.now());
                    logger.debug("Setting '{}' last movement date to {}", thing.getLabel(), now);
                    updateState(lastMovementChannel.getUID(), now);
                }
            } else {
                if (movingChannel != null) {
                    // Setting moving channel to false
                    logger.debug("Setting '{}' moving state to {}", thing.getLabel(), OnOffType.OFF);
                    updateState(movingChannel.getUID(), OnOffType.OFF);
                }

                if (state == VeluxState.DONE) {
                    if (positionChannel != null) {
                        Integer pctClosed = currentPosition.getPosition();
                        org.eclipse.smarthome.core.types.State itemState = UnDefType.UNDEF;
                        if (pctClosed == null) {
                            logger.debug(
                                    "'{}' position is currently unknown. Need to wait for an activation for KLF200 to learn its position.",
                                    thing.getLabel());
                        } else {

                            logger.debug("'{}' is currently {}% closed.", thing.getLabel(), pctClosed);
                            itemState = new PercentType(pctClosed);
                        }
                        updateState(positionChannel.getUID(), itemState);
                    }

                }
            }
        } else {
            logger.debug("Received an event for a non-configured Thing (NodeId: {})", nodeId);
        }
    }

    /**
     * Given a thing UID and an instance ID of that thing, try to find it among the list of things that we manage.
     *
     * @param instance instance ID (eg: nodeId)
     * @return The thing object or null of nothing was found.
     */
    protected Thing findThing(int instance) {
        List<Thing> things = getThing().getThings();
        for (Thing t : things) {
            if (String.valueOf(instance).equals(t.getUID().getId())) {
                logger.trace("Found thing requested: {}", t);
                return t;
            }
        }
        return null;
    }

    /**
     * Returns a reference to the CommandProcessor for the KLF200 unit.
     *
     * @return KLF200 Command Processor reference.
     */
    public KLFCommandProcessor getKLFCommandProcessor() {
        return this.klf200;
    }
}