/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.veluxklf200.internal.VeluxKLF200V2BindingConstants;
import org.openhab.binding.veluxklf200.internal.commands.CommandStatus;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetNodeInformation;
import org.openhab.binding.veluxklf200.internal.commands.KlfCmdSendCommand;
import org.openhab.binding.veluxklf200.internal.components.VeluxCommandInstruction;
import org.openhab.binding.veluxklf200.internal.components.VeluxPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles interactions relating to Vertical Interior Blinds
 *
 * @author MFK - Initial Contribution
 */
public class KLF200ShutterLikeHandler extends KLF200BaseThingHandler {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(KLF200ShutterLikeHandler.class);

    /**
     * Constructor
     *
     * @param thing thing
     */
    public KLF200ShutterLikeHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        logger.debug("updateStatus({}, {})", this.getThing().getLabel(), status);
        // TODO Auto-generated method stub
        super.updateStatus(status, statusDetail, description);
    }

    /*
     *
     * @see
     * org.eclipse.smarthome.core.thing.binding.ThingHandler#handleCommand(org.eclipse.smarthome.core.thing.ChannelUID,
     * org.eclipse.smarthome.core.types.Command)
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("handleCommand({}, {})", channelUID, command);

        int nodeId = (byte) Integer.valueOf(channelUID.getThingUID().getId()).intValue();
        String thingLabel = getThing().getLabel();

        if (command == RefreshType.REFRESH) {
            logger.debug("Handling state refresh command for {}, Id: {}.", thingLabel, nodeId);
            switch (channelUID.getId()) {
                case VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID: {
                    logger.debug("Updating state for {}, Id: {}", thingLabel, nodeId);
                    KlfCmdGetNodeInformation getNodeInfoCmd = new KlfCmdGetNodeInformation((byte) nodeId);
                    getKLFCommandProcessor().executeCommand(getNodeInfoCmd);
                    if (getNodeInfoCmd.getStatus() == CommandStatus.COMPLETE) {
                        if (getNodeInfoCmd.getNode().getCurrentPosition().isUnknown()) {
                            logger.debug(
                                    "{}, Id: {} position is currently unknown. Need to wait for an activation for KLF200 to learn its position.",
                                    thingLabel, nodeId);
                            updateState(channelUID, UnDefType.UNDEF);
                        } else {
                            int pctClosed = getNodeInfoCmd.getNode().getCurrentPosition().getPercentageClosedAsInt();
                            logger.debug("{}, Id: {} is currently {}% closed.", thingLabel, nodeId, pctClosed);
                            updateState(channelUID, new PercentType(pctClosed));
                        }
                    } else {
                        logger.error("Failed to retrieve information about node {}, error detail: {}",
                                getNodeInfoCmd.getNodeId(), getNodeInfoCmd.getStatus().getErrorDetail());
                    }
                    break;
                }
                default: {
                    logger.error("Unknown channel: {}", channelUID.getId());
                    break;
                }
            }
        } else {
            logger.debug("Handling state change command for {}, Id: {}.", thingLabel, nodeId);
            switch (channelUID.getId()) {
                case VeluxKLF200V2BindingConstants.VELUX_POSITION_CHANNEL_ID: {
                    logger.info("Trigger movement for {} to position '{}'.", thingLabel, command);

                    if ((command instanceof StopMoveType) && (command == StopMoveType.STOP)) {
                        logger.debug("Attempting to stop actuation of {}, Id:{}.", thingLabel, nodeId);
                        getKLFCommandProcessor()
                                .executeCommandAsync(new KlfCmdSendCommand(new VeluxCommandInstruction((byte) nodeId,
                                        KlfCmdSendCommand.MAIN_PARAMETER, KlfCmdSendCommand.STOP_PARAMETER)));
                    } else {
                        int targetPctClosed;
                        if (command == UpDownType.DOWN) {
                            targetPctClosed = 100;
                        } else if (command == UpDownType.UP) {
                            targetPctClosed = 0;
                        } else if (command instanceof PercentType) {
                            targetPctClosed = (int) ((PercentType) command).doubleValue();
                        } else {
                            logger.error("Invalid command value: {}", command);
                            return;
                        }

                        logger.debug("Moving {}, Id:{} to {}% closed.", thingLabel, nodeId, targetPctClosed);
                        getKLFCommandProcessor().executeCommandAsync(new KlfCmdSendCommand(
                                new VeluxCommandInstruction((byte) nodeId, KlfCmdSendCommand.MAIN_PARAMETER,
                                        VeluxPosition.setPercentClosed(targetPctClosed))));
                    }
                    break;
                }
                default: {
                    logger.error("Unknown channel: {}", channelUID.getId());
                    break;
                }
            }
        }
    }
}