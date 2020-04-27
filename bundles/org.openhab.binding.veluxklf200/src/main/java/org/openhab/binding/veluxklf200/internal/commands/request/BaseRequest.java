package org.openhab.binding.veluxklf200.internal.commands.request;

import java.io.IOException;

import org.openhab.binding.veluxklf200.internal.commands.KlfCmdGetNodeInformation;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRequest {
    private final static Logger logger = LoggerFactory.getLogger(KlfCmdGetNodeInformation.class);
    private KLFCommandProcessor processor;
    private KLFGatewayCommands command;
    private KLFCommandFrame commandFrame;

    public BaseRequest(KLFCommandProcessor processor, KLFGatewayCommands command) {
        this.processor = processor;
        this.command = command;

        this.commandFrame = new KLFCommandFrame(command);
    }

    // protected abstract void GetData(ByteBuffer dataBuffer);

    protected abstract void getData(KLFCommandFrame commandFrame);

    public void Execute() {
        this.getData(this.commandFrame);
        byte[] slipFrame = commandFrame.buildSlipFrame();

        try {
            this.processor.klfOutputStream.write(slipFrame);
            this.processor.klfOutputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int poqsd = 0;

        /*
         * try {
         * this.wait();
         * } catch (InterruptedException e) {
         * logger.error("Request wait interrupted: {}", e.getMessage());
         * }
         */
    }
}
