package org.openhab.binding.veluxklf200.internal.commands.response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;

@NonNullByDefault
public class KLFCommandFrame {
    private GatewayCommands command;
    private final static byte PROTOCOL_ID = 0x00;
    private final static int MAX_DATA_LENGTH = 250;
    private ByteBuffer byteBuffer;

    public KLFCommandFrame(GatewayCommands command) {
        this.command = command;
        this.byteBuffer = ByteBuffer.allocate(MAX_DATA_LENGTH + 2); // data length + 2 bytes for command
        this.writeShort(this.command.getNumber());
    }

    private KLFCommandFrame(byte[] rawData) {
        this.byteBuffer = ByteBuffer.wrap(rawData);
        this.command = this.readCommand();
    }

    public static KLFCommandFrame fromSlipFrame(byte[] slipFrame) throws IOException {
        byte[] transportFrame = SlipUtils.Unwrap(slipFrame);

        // Verify ProtocolID
        byte protocolID = transportFrame[0];
        if (protocolID != PROTOCOL_ID) {
            throw new IOException(String.format("ProtocolID must be %d", PROTOCOL_ID));
        }

        // Verify checksum
        byte claimedChecksum = transportFrame[transportFrame.length - 1];
        byte computedChecksum = ComputeChecksum(transportFrame);
        if (computedChecksum != claimedChecksum) {
            throw new IOException(String.format("Illegal checksum received. Awaited %d, received %d.", computedChecksum,
                    claimedChecksum));
        }

        // Extract command and data
        int length = transportFrame[1] & 0xFF; // unsigned byte
        byte[] commandFrameBytes = new byte[length - 1]; // checksum is counted in length
        System.arraycopy(transportFrame, 2, commandFrameBytes, 0, length - 1);

        KLFCommandFrame commandFrame = new KLFCommandFrame(commandFrameBytes);
        return commandFrame;
    }

    public GatewayCommands getCommand() {
        return this.command;
    }

    public void writeByte(byte value) {
        this.byteBuffer.put(value);
    }

    public void writeShort(short value) {
        this.byteBuffer.putShort(value);
        // this.byteArray.add((byte) (value >>> 8));// Shift 8 bits to get most significant byte
        // this.byteArray.add((byte) value); // Least significant byte
    }

    public void writeUnsignedShort(int value) {
        this.writeShort((short) (value & 0xFFFF));
    }

    public void writeInt(int value) {
        this.byteBuffer.putInt(value);
        // this.dataBuffer[index - 1] = (byte) (value >>> 32); // Shift 8 bits to get most significant byte
        // this.dataBuffer[index] = (byte) (value >>> 16);
        // this.dataBuffer[index + 1] = (byte) (value >>> 8);
        // this.dataBuffer[index + 2] = (byte) (value & 0xFF); // Least significant byte
    }

    public void writeLong(long value) {
        this.byteBuffer.putLong(value);

        // this.dataBuffer[index - 1] = (byte) (value >>> 32); // Shift 8 bits to get most significant byte
        // this.dataBuffer[index] = (byte) (value >>> 16);
        // this.dataBuffer[index + 1] = (byte) (value >>> 8);
        // this.dataBuffer[index + 2] = (byte) (value & 0xFF); // Least significant byte
    }

    public void writeUtf8String(String data, int byteLength) {
        byte[] buffer = new byte[byteLength];
        byte[] strBytes = data.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(strBytes, 0, buffer, 0, strBytes.length); // copy the buffer in a larger one to fill with 0

        this.byteBuffer.put(buffer, 0, buffer.length);
    }

    private GatewayCommands readCommand() {
        // reads the first two bytes of command frame
        return GatewayCommands.fromNumber(this.byteBuffer.getShort(0));
    }

    public byte readByte(int index) {
        return this.byteBuffer.get(index + 1);
    }

    public int readByteAsInt(int index) {
        return this.readByte(index) & 0xFF;
    }

    public byte[] readBytes(int index, int length) {
        byte[] buff = new byte[length];
        for (int i = 0; i < length; i++) {
            buff[i] = this.byteBuffer.get(index + 1 + i);
        }
        return buff;
    }

    public short readShort(int index) {
        return this.byteBuffer.getShort(index + 1);
    }

    public int readShortAsInt(int index) {
        return this.readShort(index) & 0xFFFF;
    }

    public int readInt(int index) {
        return this.byteBuffer.getInt(index + 1);
    }

    public long readLong(int index) {
        return this.byteBuffer.getLong(index + 1);
    }

    public String readString(int index, int length) {
        byte[] data = this.readBytes(index, length);

        // Get string length (ends at first null byte)
        int strLength;
        for (strLength = 0; strLength < data.length && data[strLength] != 0; strLength++) {
        }
        return new String(data, 0, strLength, StandardCharsets.UTF_8);
    }

    /**
     * Add protocol, length and checksum to command frame.
     *
     * @param data The command frame.
     *
     * @return Byte array holding the transport frame.
     */
    private byte[] buildTransportFrame() {
        // read the byteBuffer position to get it's real length
        int dataLength = this.byteBuffer.position();

        if (dataLength > MAX_DATA_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Max data size is %d bytes, received %d bytes", MAX_DATA_LENGTH, dataLength));
        }

        // Copy to a sized byte array
        byte[] data = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) {
            data[i] = this.byteBuffer.get(i);
        }

        byte[] transportFrame = new byte[data.length + 3]; // Add 3 bytes for protocol, length and checksum

        transportFrame[0] = PROTOCOL_ID; // Protocol ID
        transportFrame[1] = (byte) (data.length + 1); // Command Frame Length
        // transportFrame[2] = (byte) (this.command.getNumber() >>> 8); // first byte of command
        // transportFrame[3] = (byte) this.command.getNumber(); // second byte of command

        // Copy data to transportFrame at position 2
        System.arraycopy(data, 0, transportFrame, 2, data.length);

        transportFrame[transportFrame.length - 1] = ComputeChecksum(transportFrame); // checksum byte

        return transportFrame;
    }

    public byte[] buildSlipFrame() {
        byte[] transportFrame = buildTransportFrame();
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
