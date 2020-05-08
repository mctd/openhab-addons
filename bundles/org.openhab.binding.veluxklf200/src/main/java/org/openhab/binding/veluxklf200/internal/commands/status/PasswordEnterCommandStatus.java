package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum PasswordEnterCommandStatus {
    /** The request was successful. */
    SUCCESS((byte) 0),
    /** The request failed. */
    FAILURE((byte) 1);

    private byte code;
    private final static Logger logger = LoggerFactory.getLogger(PasswordEnterCommandStatus.class);

    PasswordEnterCommandStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PasswordEnterCommandStatus fromCode(byte code) {
        for (PasswordEnterCommandStatus testCommand : values()) {
            if (testCommand.getCode() == code) {
                return testCommand;
            }
        }

        logger.error("Invalid code: {}", code);
        return FAILURE;
    }
}
