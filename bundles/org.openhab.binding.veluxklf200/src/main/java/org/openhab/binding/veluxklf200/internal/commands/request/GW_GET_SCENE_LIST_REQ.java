package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_GET_SCENE_LIST_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;

/**
 * Request a list of scenes.
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_GET_SCENE_LIST_REQ extends BaseRequest<GW_GET_SCENE_LIST_CFM> {
    public GW_GET_SCENE_LIST_REQ() {
        super(GatewayCommands.GW_GET_SCENE_LIST_REQ);
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        // No data
    }

    @Override
    public boolean handleResponseImpl(GW_GET_SCENE_LIST_CFM response) {
        return true;
    }
}
