package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_ALL_NODES_INFORMATION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;

/**
 * Request extended information of all nodes.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_ALL_NODES_INFORMATION_REQ extends BaseRequest<GW_GET_ALL_NODES_INFORMATION_CFM> {
    public GW_GET_ALL_NODES_INFORMATION_REQ() {
        super(GatewayCommands.GW_GET_ALL_NODES_INFORMATION_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_GET_ALL_NODES_INFORMATION_CFM response) {
        return true;
    }
}
