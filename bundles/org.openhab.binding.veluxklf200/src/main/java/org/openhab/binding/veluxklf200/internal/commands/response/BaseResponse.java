package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;

public abstract class BaseResponse {
    private KLFCommandProcessor processor;
    private KLFCommandFrame commandFrame;

    public BaseResponse(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        this.processor = processor;
        this.commandFrame = commandFrame;
    }

    protected KLFCommandProcessor getProcessor() {
        return this.processor;
    }

    protected KLFCommandFrame getCommandFrame() {
        return this.commandFrame;
    }
}
