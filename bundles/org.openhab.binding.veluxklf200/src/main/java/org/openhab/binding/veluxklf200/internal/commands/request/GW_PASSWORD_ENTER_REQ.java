package org.openhab.binding.veluxklf200.internal.commands.request;

import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;

public class GW_PASSWORD_ENTER_REQ extends BaseRequest {

    private String password;

    public GW_PASSWORD_ENTER_REQ(String password) {
        super(KLFGatewayCommands.GW_PASSWORD_CHANGE_REQ);
        this.password = password;
    }

    protected KLFCommandFrame getCommandFrame() {
        // TODO : create the command Frame
        return null;
    }
}
