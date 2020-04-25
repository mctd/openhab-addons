package org.openhab.binding.veluxklf200.internal.commands.request;

import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetNodeInformation;
import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRequest {
    private final static Logger logger = LoggerFactory.getLogger(KlfCmdGetNodeInformation.class);

    public BaseRequest() {
    }

    protected abstract void Pack();

    public void Execute() {

        this.getTransportFrame();

        try {
            this.wait();
        } catch (InterruptedException e) {
            logger.error("Request wait interrupted: {}", e.getMessage());
        }
    }

    private KLFCommandFrame getCommandFrame() {
        // KLFCommandFrame commandFrame = KLFCommandFrame.
        return null;
    }

    private void getTransportFrame() {

    }
}
