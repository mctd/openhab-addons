package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;

public abstract class BaseResponse {
    private KLFCommandFrame commandFrame;

    public BaseResponse(KLFCommandFrame commandFrame) {
        this.commandFrame = commandFrame;
    }

    protected KLFCommandFrame getCommandFrame() {
        return this.commandFrame;
    }
}
