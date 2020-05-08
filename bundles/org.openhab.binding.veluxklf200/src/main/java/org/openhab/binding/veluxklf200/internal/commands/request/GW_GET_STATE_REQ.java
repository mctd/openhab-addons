package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_STATE_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;

/**
 * Request the state of the gateway.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_STATE_REQ extends BaseRequest<GW_GET_STATE_CFM> {

    public GW_GET_STATE_REQ() {
        super(GatewayCommands.GW_GET_STATE_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_GET_STATE_CFM response) {
        return true;
    }
}
