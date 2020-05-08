package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum ErrorState {
    /** Not further defined error. */
    NOT_DEFINED((byte) 0, "Not further defined error."),
    /** Unknown Command or command is not accepted at this state. */
    UNKNOWN_COMMAND((byte) 1, "Unknown Command or command is not accepted at this state."),
    /** ERROR on Frame Structure. */
    FRAME_ERROR((byte) 2, "ERROR on Frame Structure."),
    /** Busy. Try again later. */
    BUSY((byte) 7, "Busy. Try again later"),
    /** Bad system table index. */
    BAD_INDEX((byte) 8, "Bad system table index"),
    /** Not authenticated. */
    NOT_AUTHENTICATED((byte) 12, "Not authenticated");

    private static final Logger logger = LoggerFactory.getLogger(ErrorState.class);
    private byte code;
    private String description;

    ErrorState(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static ErrorState fromCode(byte code) {
        for (ErrorState testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return NOT_DEFINED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
