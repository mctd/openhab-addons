package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.utility.KLFCommandFrame;

public class TransportLayer {
    private static final byte PROTOCOL_ID = 0;

    private TransportLayer() {

    }

    public byte getLength() {
        return 0;
    }

    public KLFCommandFrame getCommandFrame() {
        return null;
    }

    public byte getChecksum() {
        return 0;
    }

}
