package org.openhab.binding.veluxklf200.internal.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PasswordEnterCommandStatus {
    /** The request was successful. */
    SUCCESS((byte) 0),
    /** The request failed. */
    FAILURE((byte) 1);

    private byte status;
    private final static Logger logger = LoggerFactory.getLogger(PasswordEnterCommandStatus.class);

    PasswordEnterCommandStatus(byte status) {
        this.status = status;
    }

    public byte getCode() {
        return this.status;
    }

    public static PasswordEnterCommandStatus fromCode(byte code) {
        for (PasswordEnterCommandStatus testCommand : values()) {
            if (testCommand.getCode() == code) {
                return testCommand;
            }
        }
        logger.warn("Invalid code: {}", code);
        return null; // TODO : throw exception ?
    }
}
