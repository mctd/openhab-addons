package org.openhab.binding.veluxklf200.internal.utility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KLFCommandFrame {
    private KLFGatewayCommands command;
    private byte[] data;
    private ByteBuffer dataBuffer;
    private final static byte PROTOCOL_ID = 0x00;
    private static final byte SLIP_BYTE_END = (byte) 0xC0;
    private static final byte SLIP_BYTE_ESC = (byte) 0xDB;
    private static final byte SLIP_BYTE_ESC_END = (byte) 0xDC;
    private static final byte SLIP_BYTE_ESC_ESC = (byte) 0xDD;

    private final static Logger logger = LoggerFactory.getLogger(KLFCommandFrame.class);

    private KLFCommandFrame(byte[] rawData) {
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

    public byte[] getTransportFrame() {
        byte commandFrameLength = (byte) (KLFUtils.sizeof(PROTOCOL_ID) + KLFUtils.sizeof(this.command.getNumber())
                + this.data.length);

        byte[] transportFrame = new byte[commandFrameLength + 3];

        transportFrame[0] = PROTOCOL_ID; // Protocol ID
        transportFrame[1] = commandFrameLength; // Command Frame Length
        transportFrame[2] = (byte) (this.command.getNumber() >>> 8); // first byte of command
        transportFrame[3] = (byte) this.command.getNumber(); // second byte of command

        // Copy data
        System.arraycopy(this.data, 0, transportFrame, 4, this.data.length);

        byte checksum = 0;
        for (byte b : transportFrame) {
            checksum = (byte) (checksum ^ b); // XOR all bytes
        }

        transportFrame[transportFrame.length - 1] = checksum; // checksum byte

        return transportFrame;
    }

    public static KLFCommandFrame fromSlipFrame(byte[] slipFrame) throws IOException {
        byte[] transportFrame = slipUnwrap(slipFrame);
        byte protocolID = transportFrame[0];

        // Verify ProtocolID
        if (protocolID != PROTOCOL_ID) {
            throw new IOException(String.format("ProtocolID must be %d", PROTOCOL_ID));
        }

        byte checksum = transportFrame[transportFrame.length - 1];

        // Verify checksum
        // The Checksum are made by bitwise XOR all bytes from and including the ProtocolID parameter to last data byte
        byte computedChecksum = 0;
        for (int i = 0; i < transportFrame.length - 1; i++) {
            computedChecksum = (byte) (computedChecksum ^ transportFrame[i]); // XOR all bytes
        }
        if (computedChecksum != checksum) {
            throw new IOException(
                    String.format("Illegal checksum received. Awaited %d, received %d.", computedChecksum, checksum));
        }

        byte length = transportFrame[1];
        byte[] commandData = new byte[length - 1];
        System.arraycopy(transportFrame, 2, commandData, 0, length - 1);

        KLFCommandFrame commandFrame = new KLFCommandFrame(commandData);
        return commandFrame;
    }

    private static byte[] slipUnwrap(byte[] packet) {
        if (packet.length < 3) {
            logger.error("Attempt to decode a packet that is too short: {}", packet.length);
            return null;
        }
        if (packet[0] != SLIP_BYTE_END) {
            logger.error("Attempt to decode a packet with an unexpected character at position 0");
            return null;
        }
        ;
        if (packet[packet.length - 1] != SLIP_BYTE_END) {
            logger.error("Attempt to decode a packet with an unexpected end character");
            return null;
        }
        ;
        int additional = -2; // will remove first and last bytes
        for (int i = 0; i < packet.length; i++) {
            if (packet[i] == SLIP_BYTE_ESC) {
                additional--; // will remove one byte for each esc'ed byte
            }
        }
        byte[] payload = new byte[packet.length + additional];

        int packetIndex = 0;
        for (int i = 0; i < packet.length; i++) {
            if ((i == 0) || (i == packet.length - 1)) {
                continue;
            }
            if ((packet[i] == SLIP_BYTE_ESC) && (packet[i + 1] == SLIP_BYTE_ESC_ESC)) {
                // unesc the byte
                payload[packetIndex++] = SLIP_BYTE_ESC;
                i++;
            } else if ((packet[i] == SLIP_BYTE_ESC) && (packet[i + 1] == SLIP_BYTE_ESC_END)) {
                // unesc the byte
                payload[packetIndex++] = SLIP_BYTE_END;
                i++;
            } else {
                // copy the byte as is
                payload[packetIndex++] = packet[i];
            }
        }
        return payload;
    }
}
