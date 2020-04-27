package org.openhab.binding.veluxklf200.internal.commands.request;

import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;

public class GW_PASSWORD_ENTER_REQ extends BaseRequest {

    private String password;

    public GW_PASSWORD_ENTER_REQ(KLFCommandProcessor processor, String password) {
        super(processor, KLFGatewayCommands.GW_PASSWORD_ENTER_REQ);
        this.password = password;
    }

    protected KLFCommandFrame getCommandFrame() {
        // TODO : create the command Frame
        return null;
    }

    @Override
    protected void getData(KLFCommandFrame commandFrame) {
        commandFrame.writeUtf8String(this.password, 32);
    }
}
