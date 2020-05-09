package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum ExecutionState {
    /**
     * This status information is only returned about an ACTIAVTE_FUNC, an ACTIVATE_MODE, an ACTIVATE_STATE or a WINK
     * command. The parameter is unable to execute due to given conditions. An example can be that the temperature is
     * too high. It indicates that the parameter could not execute per the contents of the present activate command.
     */
    NON_EXECUTING((byte) 0, "Non executing"),
    /**
     * This status information is only returned about an ACTIVATE_STATUS_REQ command. An error has occurred while
     * executing. This error information will be cleared the next time the parameter is going into ‘Waiting for
     * executing’, ‘Waiting for power’ or ‘Executing’. A parameter can have the execute status ‘Error while executing’
     * only if the previous execute status was ‘Executing’. Note that this execute status gives information about the
     * previous execution of the parameter, and gives no indication whether the following execution will fail.
     */
    ERROR_EXECUTING((byte) 1, "Error while execution"),
    /** Not used. */
    NOT_USED((byte) 2, "Not used"),
    /** The parameter is waiting for power to proceed execution. */
    WAITING_POWER((byte) 3, "Waiting for power"),
    /** Execution for the parameter is in progress */
    EXECUTING((byte) 4, "Executing"),
    /**
     * The parameter is not executing and no error has been detected. No activation of the parameter has been initiated.
     * The parameter is ready for activation.
     */
    DONE((byte) 5, "Done"),
    /** The state is unknown. */
    UNKNOWN((byte) 255, "Unknown");

    private static final Logger logger = LoggerFactory.getLogger(ExecutionState.class);
    private byte code;
    private String description;

    private ExecutionState(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static ExecutionState fromCode(byte code) {
        for (ExecutionState testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
