package org.openhab.binding.veluxklf200.internal.status;

/**
 * Contains the execution status of the node
 *
 * @author emmanuel
 *
 */
public enum RunStatus {
    /** Execution is completed with no errors. */
    EXECUTION_COMPLETED((byte) 0, "Execution is completed with no errors"),
    /** Execution has failed. (Get specifics in the following error code). */
    EXECUTION_FAILED((byte) 1, "Execution has failed. (Get specifics in the following error code)"),
    /** Execution is still active. */
    EXECUTION_ACTIVE((byte) 2, "Execution is still active");

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

        // TODO : log warning for unmapped values
        return null; // TODO: throw exception ?
    }

    @Override
    public String toString() {
        return this.description;
    }
}
