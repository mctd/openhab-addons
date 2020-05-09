/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.veluxklf200.internal.VeluxKlf200BridgeConfiguration;
import org.openhab.binding.veluxklf200.internal.commands.request.BaseRequest;
import org.openhab.binding.veluxklf200.internal.commands.request.GW_REBOOT_REQ;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_REBOOT_CFM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles a KLF200 Unit as a bridge.
 *
 * @author emmanuel
 */
@NonNullByDefault
public class VeluxKlf200BridgeHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(VeluxKlf200BridgeHandler.class);

    private final static int RECONNECT_INTERVAL = 10;

    // private VeluxKlf200BridgeConfiguration bridgeConfig;
    private @Nullable VeluxKlf200Connection connection;
    private @Nullable ScheduledFuture<?> connectJob;

    /**
     * Constructor
     *
     * @param bridge the bridge
     */
    public VeluxKlf200BridgeHandler(Bridge bridge) {
        super(bridge);
    }

    public VeluxKlf200BridgeConfiguration getConfiguration() {
        return getConfigAs(VeluxKlf200BridgeConfiguration.class);
    }

    public @Nullable VeluxKlf200Connection getConnection() {
        return this.connection;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.error("Bridge cannot handle commands. Asked {} for channel: {}", command, channelUID);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing bridge handler for bridge Id: {}.", this.getThing().getUID().getId());

        this.connection = new VeluxKlf200Connection(this, this.scheduler);

        startConnectJob();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing the bridge handler.");
        updateStatus(ThingStatus.OFFLINE);

        stopConnectJob();
    }

    private void startConnectJob() {
        ScheduledFuture<?> localJob = this.connectJob;
        if (localJob == null || localJob.isCancelled()) {
            this.connectJob = scheduler.scheduleWithFixedDelay(this.connection, 0, RECONNECT_INTERVAL,
                    TimeUnit.SECONDS);
        }
    }

    private void stopConnectJob() {
        ScheduledFuture<?> localJob = this.connectJob;
        if (localJob != null && !localJob.isCancelled()) {
            localJob.cancel(true);
        }
        this.connectJob = null;
        this.connection = null;
    }

    public void sendRequest(BaseRequest<?> request) {
        VeluxKlf200Connection connection = this.getConnection();
        if (connection == null) {
            logger.warn("Cannot send request {}: connection down", request);
        } else {
            connection.sendRequest(request);
        }
    }

    @Override
    public void handleConfigurationUpdate(Map<@NonNull String, @NonNull Object> configurationParameters) {
        if (((boolean) configurationParameters.getOrDefault("reboot", false))) {
            logger.warn("Sending reboot command to KLF unit");

            GW_REBOOT_REQ rebootReq = new GW_REBOOT_REQ();
            this.sendRequest(rebootReq);
            GW_REBOOT_CFM rebootResponse = rebootReq.getResponse();
            if (rebootResponse != null) {
                this.dispose();
                // Thread.sleep(5000); // needed?
                this.initialize();
            }
        }

        // Force action switches values to false
        Map<@NonNull String, @NonNull Object> newConfig = new HashMap<String, Object>(configurationParameters);
        newConfig.put("reboot", false);

        super.handleConfigurationUpdate(newConfig);
    }

    @Override
    // public override to make visible to connection manager
    public void updateStatus(@NonNull ThingStatus status, @NonNull ThingStatusDetail statusDetail,
            @Nullable String description) {
        logger.debug("updateStatus({}, {}, {})", status, statusDetail, description);
        super.updateStatus(status, statusDetail, description);
    }

    public void updateProperties(@Nullable String softwareVersion, @Nullable String hardwareVersion,
            @Nullable String productGroup, @Nullable String productType, @Nullable String protocol,
            @Nullable String state) {
        Map<String, String> properties = this.editProperties();
        if (softwareVersion != null) {
            properties.put("Software Version", softwareVersion);
        }
        if (hardwareVersion != null) {
            properties.put("Hardware Version", hardwareVersion);
        }
        if (productGroup != null) {
            properties.put("Product Group", productGroup.toString());
        }
        if (productType != null) {
            properties.put("Product Type", productType.toString());
        }
        if (protocol != null) {
            properties.put("Protocol", protocol);
        }
        if (state != null) {
            properties.put("State", state);
        }
        this.updateProperties(properties);
    }
}