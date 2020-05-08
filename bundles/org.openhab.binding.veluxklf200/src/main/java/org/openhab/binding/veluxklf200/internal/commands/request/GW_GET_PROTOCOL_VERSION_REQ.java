package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_PROTOCOL_VERSION_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;

/**
 * Request KLF 200 API protocol version.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_PROTOCOL_VERSION_REQ extends BaseRequest<GW_GET_PROTOCOL_VERSION_CFM> {
    public GW_GET_PROTOCOL_VERSION_REQ() {
        super(GatewayCommands.GW_GET_PROTOCOL_VERSION_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_GET_PROTOCOL_VERSION_CFM response) {
        return true;
    }

}
