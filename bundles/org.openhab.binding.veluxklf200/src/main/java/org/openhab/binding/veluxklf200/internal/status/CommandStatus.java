package org.openhab.binding.veluxklf200.internal.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CommandStatus {
    /** Command is rejected */
    REJECTED((byte) 0, "Command is rejected"),
    /** Command is accepted. */
    ACCEPTED((byte) 1, "Command is accepted"),
    /** Not defined */
    UNDEFINED((byte) 0xFF, "Not defined");

    private byte status;
    private String label;
    private final static Logger logger = LoggerFactory.getLogger(CommandStatus.class);

    CommandStatus(byte status, String statusLabel) {
        this.status = status;
        this.label = statusLabel;
    }

    public byte getCode() {
        return this.status;
    }

    public static CommandStatus fromCode(byte code) {
        for (CommandStatus testCommand : values()) {
            if (testCommand.getCode() == code) {
                return testCommand;
            }
        }
        logger.warn("Invalid code: {}", code);
        return UNDEFINED;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
