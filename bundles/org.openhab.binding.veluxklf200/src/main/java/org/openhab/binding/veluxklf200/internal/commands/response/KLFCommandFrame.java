package org.openhab.binding.veluxklf200.internal.commands.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KLFCommandFrame {
    private KLFGatewayCommands command;
    private ByteBuffer dataBuffer;
    private final static byte PROTOCOL_ID = 0x00;
    private final static int MAX_DATA_LENGTH = 250;

    private final static Logger logger = LoggerFactory.getLogger(KLFCommandFrame.class);

    public KLFCommandFrame(KLFGatewayCommands command) {
        byte[] data = new byte[100];
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        DataInputStream ds = new DataInputStream(bais);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(255);
        // TODO : create a byte[] writer ...
    }

    private KLFCommandFrame(byte[] rawData) {
        // TODO: create a byte array and read/write from it rather than using ByteBuffer
        this.dataBuffer = ByteBuffer.wrap(rawData);

        short commandNumber = this.dataBuffer.getShort(0);
        this.command = KLFGatewayCommands.fromNumber(commandNumber);

        // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    }

    public KLFGatewayCommands getCommand() {
        return this.command;
    }

    public byte getByte(int index) {
        return this.dataBuffer.get(index + 1);
    }

    public byte[] getBytes(int index, int length) {
        byte[] buff = new byte[length];
        for (int i = 0; i < length; i++) {
            buff[i] = this.dataBuffer.get(index + 1 + i);
        }
        return buff;
    }

    public short getShort(int index) {
        return this.dataBuffer.getShort(index + 1);
    }

    public int getInt(int index) {
        return this.dataBuffer.getInt(index + 1);
    }

    public long getLong(int index) {
        return this.dataBuffer.getLong(index + 1);
    }

    public String getString(int index, int length) {
        byte[] data = this.getBytes(index, length);

        // Get string length (ends at first null byte)
        int strLength;
        for (strLength = 0; strLength < data.length && data[strLength] != 0; strLength++) {
        }
        return new String(data, 0, strLength, StandardCharsets.UTF_8);
    }

    public static KLFCommandFrame fromSlipFrame(byte[] slipFrame) throws IOException {
        byte[] transportFrame = SlipUtils.Unwrap(slipFrame);
        byte protocolID = transportFrame[0];

        // Verify ProtocolID
        if (protocolID != PROTOCOL_ID) {
            throw new IOException(String.format("ProtocolID must be %d", PROTOCOL_ID));
        }

        byte claimedChecksum = transportFrame[transportFrame.length - 1];

        // Verify checksum
        byte computedChecksum = ComputeChecksum(transportFrame);
        if (computedChecksum != claimedChecksum) {
            throw new IOException(String.format("Illegal checksum received. Awaited %d, received %d.", computedChecksum,
                    claimedChecksum));
        }

        byte length = transportFrame[1];
        byte[] commandData = new byte[length - 1];
        System.arraycopy(transportFrame, 2, commandData, 0, length - 1);

        KLFCommandFrame commandFrame = new KLFCommandFrame(commandData);
        return commandFrame;
    }

    private byte[] getTransportFrame(byte[] data) {
        if (null == data) {
            throw new IllegalArgumentException("null data packet is not allowed");
        }
        if (data.length > MAX_DATA_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Max data size is %d bytes, received %d bytes", MAX_DATA_LENGTH, data.length));
        }

        // Length is data length + 2 bytes for command code
        byte commandFrameLength = (byte) (data.length + 2);

        byte[] transportFrame = new byte[commandFrameLength + 3];

        transportFrame[0] = PROTOCOL_ID; // Protocol ID
        transportFrame[1] = commandFrameLength; // Command Frame Length
        transportFrame[2] = (byte) (this.command.getNumber() >>> 8); // first byte of command
        transportFrame[3] = (byte) this.command.getNumber(); // second byte of command

        // Copy data
        System.arraycopy(data, 0, transportFrame, 4, data.length);

        transportFrame[transportFrame.length - 1] = ComputeChecksum(transportFrame); // checksum byte

        return transportFrame;
    }

    private byte[] getSlipPackedFrame(byte[] data) {
        byte[] transportFrame = getTransportFrame(data);
        return SlipUtils.Wrap(transportFrame);
    }

    /**
     * Compute checksum.
     *
     * @param data Input data.
     *
     * @return The computed checksum.
     */
    private static byte ComputeChecksum(byte[] data) {
        // The Checksum are made by bitwise XOR all bytes from and including the ProtocolID parameter to last data byte
        // (thus, ignoring the last byte as it contains the checksum itself)
        byte computedChecksum = 0;
        for (int i = 0; i < data.length - 1; i++) {
            computedChecksum = (byte) (computedChecksum ^ data[i]);
        }

        return computedChecksum;
    }
}
