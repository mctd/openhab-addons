package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.GatewayState;
import org.openhab.binding.veluxklf200.internal.status.GatewaySubState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_GET_STATE_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_GET_STATE_CFM.class);

    private GatewayState gatewaySate;
    private GatewaySubState gatewaySubState;

    public GW_GET_STATE_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.gatewaySate = GatewayState.fromCode(this.getCommandFrame().readByte(1));
        this.gatewaySubState = GatewaySubState.fromCode(this.getCommandFrame().readByte(2));

        logger.info("Gateway State: {}, SubState: {}", this.getGatewayState(), this.getGatewaySubState());
    }

    public GatewayState getGatewayState() {
        return this.gatewaySate;
    }

    public GatewaySubState getGatewaySubState() {
        return this.gatewaySubState;
    }
}
