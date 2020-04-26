package org.openhab.binding.veluxklf200.internal.commands.request;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetNodeInformation;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRequest {
    private final static Logger logger = LoggerFactory.getLogger(KlfCmdGetNodeInformation.class);
    private KLFGatewayCommands command;

    public BaseRequest(KLFGatewayCommands command) {
        this.command = command;
    }

    // protected abstract void GetData(ByteBuffer dataBuffer);

    public void Execute() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(bos);
        this.getTransportFrame(dataStream);

        try {
            this.wait();
        } catch (InterruptedException e) {
            logger.error("Request wait interrupted: {}", e.getMessage());
        }
    }

    private KLFCommandFrame getCommandFrame(DataOutputStream dataStream) {
        // KLFCommandFrame commandFrame = KLFCommandFrame.

        return null;
    }

    private void getTransportFrame(DataOutputStream dataStream) {

    }
}
