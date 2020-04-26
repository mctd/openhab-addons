package org.openhab.binding.veluxklf200.internal.commands.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to handle SLIP encoded messages.
 *
 * @author emmanuel
 *
 */
public class SlipUtils {
    private static final byte SLIP_BYTE_END = (byte) 0xC0;
    private static final byte SLIP_BYTE_ESC = (byte) 0xDB;
    private static final byte SLIP_BYTE_ESC_END = (byte) 0xDC;
    private static final byte SLIP_BYTE_ESC_ESC = (byte) 0xDD;

    private final static Logger logger = LoggerFactory.getLogger(SlipUtils.class);

    private SlipUtils() {
    }

    /**
     * Wrap a data message into SLIP.
     *
     * @param payload data to be packed into SLIP.
     *
     * @return SLIP wrapped data.
     */
    public static byte[] Wrap(byte[] payload) {
        int additional = 2;
        for (byte b : payload) {
            if ((b == SLIP_BYTE_ESC) || (b == SLIP_BYTE_END)) {
                additional++;
            }
        }
        byte[] packet = new byte[payload.length + additional];
        int packetIndex = 0;
        packet[packetIndex++] = SLIP_BYTE_END;

        for (byte b : payload) {
            if (b == SLIP_BYTE_ESC) {
                packet[packetIndex++] = SLIP_BYTE_ESC;
                packet[packetIndex++] = SLIP_BYTE_ESC_ESC;
            } else if (b == SLIP_BYTE_END) {
                packet[packetIndex++] = SLIP_BYTE_ESC;
                packet[packetIndex++] = SLIP_BYTE_ESC_END;
            } else {
                packet[packetIndex++] = b;
            }
        }
        packet[packetIndex++] = SLIP_BYTE_END;
        assert (packetIndex == packet.length);
        return packet;
    }

    /**
     * Unwrap data packed into a SLIP message.
     *
     * @param slipData SLIP wrapped data.
     *
     * @return Unwrapped data.
     */
    public static byte[] Unwrap(byte[] slipData) {
        if (slipData.length < 3) {
            logger.error("Attempt to decode a packet that is too short: {}", slipData.length);
            return null;
        }
        if (slipData[0] != SLIP_BYTE_END) {
            logger.error("Attempt to decode a packet with an unexpected character at position 0");
            return null;
        }
        ;
        if (slipData[slipData.length - 1] != SLIP_BYTE_END) {
            logger.error("Attempt to decode a packet with an unexpected end character");
            return null;
        }
        ;
        int additional = -2; // will remove first and last bytes
        for (int i = 0; i < slipData.length; i++) {
            if (slipData[i] == SLIP_BYTE_ESC) {
                additional--; // will remove one byte for each esc'ed byte
            }
        }
        byte[] payload = new byte[slipData.length + additional];

        int packetIndex = 0;
        for (int i = 0; i < slipData.length; i++) {
            if ((i == 0) || (i == slipData.length - 1)) {
                continue;
            }
            if ((slipData[i] == SLIP_BYTE_ESC) && (slipData[i + 1] == SLIP_BYTE_ESC_ESC)) {
                // unesc the byte
                payload[packetIndex++] = SLIP_BYTE_ESC;
                i++;
            } else if ((slipData[i] == SLIP_BYTE_ESC) && (slipData[i + 1] == SLIP_BYTE_ESC_END)) {
                // unesc the byte
                payload[packetIndex++] = SLIP_BYTE_END;
                i++;
            } else {
                // copy the byte as is
                payload[packetIndex++] = slipData[i];
            }
        }
        return payload;
    }
}
