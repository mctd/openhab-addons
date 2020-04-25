/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.engine;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.veluxklf200.internal.commands.BaseKLFCommand;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.components.VeluxPosition;
import org.openhab.binding.veluxklf200.internal.components.VeluxState;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Under normal circumstances, the KLF200 will respond to commands that have
 * been sent to it. However, it will also issue responses (notifications) in the
 * case that something else on the network issued a command. For example, if a
 * remote control is used to operate a Velux device, the KLF will issue
 * notifications in respect of the devices movements. These are captured /
 * processed here.
 *
 * @author emmanuel
 */
public class KLFEventNotification {

    /** Logging. */
    private final Logger logger = LoggerFactory.getLogger(KLFEventNotification.class);

    /** List of response codes that we are watching out for. */
    private KLFGatewayCommands watchList[] = new KLFGatewayCommands[] {
            KLFGatewayCommands.GW_NODE_STATE_POSITION_CHANGED_NTF };

    /** List of third-parties that have registered to be notified of events. */
    private List<KLFEventListener> listeners;

    /**
     * Instantiates a new KLF event notification.
     */
    protected KLFEventNotification() {
        this.listeners = new ArrayList<KLFEventListener>();
    }

    /**
     * Called by others to notify us that an event was received that we may be
     * interested in.
     *
     * @param responseCode
     *            the response code
     * @param data
     *            the data
     */
    protected void notifyEvent(KLFGatewayCommands responseCommand, byte[] data) {
        logger.debug("Event Notified: {}, -> {}", responseCommand, data);
        switch (responseCommand) {
            case GW_NODE_STATE_POSITION_CHANGED_NTF:
                logger.trace("Handling Notification for {}.", responseCommand);

                byte nodeId = data[BaseKLFCommand.FIRSTBYTE];
                VeluxState state = VeluxState.createFromCode(data[BaseKLFCommand.FIRSTBYTE + 1]);
                VeluxPosition currentPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 2));
                VeluxPosition targetPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 4));
                VeluxPosition fp1CurrentPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 6));
                VeluxPosition fp2CurrentPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 8));
                VeluxPosition fp3CurrentPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 10));
                VeluxPosition fp4CurrentPosition = VeluxPosition
                        .createFromCode(KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 12));
                int remainingTime = KLFUtils.extractTwoBytes(data, BaseKLFCommand.FIRSTBYTE + 14);
                long timestamp = KLFUtils.extractUnsignedInt32(data, BaseKLFCommand.FIRSTBYTE + 16);

                logger.debug(
                        "Node {} position changed, state: {}, current position: {} closed, target position:{} closed, fp1CurrentPosition: {}, fp2CurrentPosition: {}, fp3CurrentPosition: {}, fp4CurrentPosition: {}, time remaining: {} seconds, timestamp: {}",
                        nodeId, state, currentPosition.toString(), targetPosition.toString(), fp1CurrentPosition,
                        fp2CurrentPosition, fp3CurrentPosition, fp4CurrentPosition, remainingTime, timestamp);

                for (KLFEventListener listen : listeners) {
                    listen.handleEvent(nodeId, state, currentPosition, targetPosition, fp1CurrentPosition,
                            fp2CurrentPosition, fp3CurrentPosition, fp4CurrentPosition, remainingTime, timestamp);
                }
                break;
            default:
                logger.error("Notified of event {}, but unable to handle it. Data: {}", responseCommand, data);
                break;
        }
    }

    /**
     * We are interested in only certain events / notifications. This checks to
     * see if a particular event (command code) is on our watch-list (something
     * that we are interested in being notified about).
     *
     * @param code
     *            the code
     * @return true, if is on watch list
     */
    public boolean isOnWatchList(KLFGatewayCommands command) {
        for (KLFGatewayCommands elem : this.watchList) {
            if (elem == command) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called by third-party that is interested in being notified when things change. This adds them to a list of
     * 'listeners' who will be notified when something happens
     *
     * @param listener Class that implements the {@link KLFEventListener} interface.
     */
    public void registerListener(KLFEventListener listener) {
        this.listeners.add(listener);
    }
}