package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.GatewayState;
import org.openhab.binding.veluxklf200.internal.commands.status.GatewaySubState;

@NonNullByDefault
public class GW_GET_STATE_CFM extends BaseConfirmationResponse {
    private GatewayState gatewaySate;
    private GatewaySubState gatewaySubState;

    public GW_GET_STATE_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.gatewaySate = GatewayState.fromCode(this.getCommandFrame().readByte(1));
        this.gatewaySubState = GatewaySubState.fromCode(this.getCommandFrame().readByte(2));
    }

    public GatewayState getGatewayState() {
        return this.gatewaySate;
    }

    public GatewaySubState getGatewaySubState() {
        return this.gatewaySubState;
    }
}
