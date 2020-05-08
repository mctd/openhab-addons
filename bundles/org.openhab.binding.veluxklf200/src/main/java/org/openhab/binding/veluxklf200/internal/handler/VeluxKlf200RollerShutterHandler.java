/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import static org.openhab.binding.veluxklf200.internal.VeluxKlf200BindingConstants.*;

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_COMMAND_SEND_REQ;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;
import org.openhab.binding.veluxklf200.internal.commands.status.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles an actuator
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200RollerShutterHandler extends VeluxKlf200BaseNodeHandler {

    private final Logger logger = LoggerFactory.getLogger(VeluxKlf200RollerShutterHandler.class);

    /**
     * Constructor
     *
     * @param thing thing
     */
    public VeluxKlf200RollerShutterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand({}, {})", channelUID, command);

        if (!CHANNEL_CONTROL.equals(channelUID.getId())) {
            return;
        }

        if (command == RefreshType.REFRESH) {
            refreshNodeInfo();
        } else {
            Position targetPosition;
            // TODO : delegate position mapping (UP/DOWN, OPEN/CLOSE, ...) to specific actuator handlers
            if (command == StopMoveType.STOP) {
                targetPosition = Position.STOP;
            } else if (command == UpDownType.DOWN || command == OpenClosedType.CLOSED) {
                targetPosition = new Position(100);
            } else if (command == UpDownType.UP || command == OpenClosedType.OPEN) {
                targetPosition = new Position(0);
            } else if (command instanceof DecimalType) {
                targetPosition = new Position(((PercentType) command).intValue());
            } else {
                logger.warn("Unable to understand command of type {}", command.getClass());
                return;
            }

            // Get desired velocity from thing config
            Velocity velocity = Velocity.DEFAULT;
            BigDecimal desiredVelocity = (BigDecimal) this.getThing().getConfiguration().get(PROP_VELOCITY);
            if (desiredVelocity != null) {
                velocity = Velocity.fromCode((byte) (desiredVelocity.intValue() & 0xFF));
            }
            logger.trace("Configured velocity for thing {}: {}", this.getThing().getUID(), velocity);

            // Build and send the command request
            GW_COMMAND_SEND_REQ sendReq = new GW_COMMAND_SEND_REQ(this.getListenedNodeId(), NodeParameter.MP, targetPosition);
            if (velocity == Velocity.FAST) {
                sendReq.setParamValue(NodeParameter.FP1, new Position(100));
            } else if (velocity == Velocity.SILENT) {
                sendReq.setParamValue(NodeParameter.FP1, new Position(0));
            }
            sendRequest(sendReq);
        }
    }
}