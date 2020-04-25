package org.openhab.binding.veluxklf200.internal.commands.response;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CommandDataBuffer {

    private ByteBuffer byteBuffer;

    public CommandDataBuffer(byte[] commandData) {
        this.byteBuffer = ByteBuffer.wrap(commandData);
        this.byteBuffer.order(ByteOrder.BIG_ENDIAN);
    }

}
