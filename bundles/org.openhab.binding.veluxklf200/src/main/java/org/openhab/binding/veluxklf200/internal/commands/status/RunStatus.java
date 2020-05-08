package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the execution status of the node
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public enum RunStatus {
    /** Execution is completed with no errors. */
    EXECUTION_COMPLETED((byte) 0, "Execution is completed with no errors"),
    /** Execution has failed. (Get specifics in the following error code). */
    EXECUTION_FAILED((byte) 1, "Execution has failed. (Get specifics in the following error code)"),
    /** Execution is still active. */
    EXECUTION_ACTIVE((byte) 2, "Execution is still active");

    private static final Logger logger = LoggerFactory.getLogger(RunStatus.class);
    private byte code;
    private String description;

    RunStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static RunStatus fromCode(byte code) {
        for (RunStatus testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return EXECUTION_COMPLETED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
