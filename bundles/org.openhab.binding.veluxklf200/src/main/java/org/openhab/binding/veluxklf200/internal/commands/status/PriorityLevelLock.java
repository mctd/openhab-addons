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
public enum PriorityLevelLock {
    /**
     * Do not set a new lock on priority level. Information in the parameters PL_0_3, PL_4_7 and LockTime are not used.
     * This is the one typically used..
     */
    NO_LOCK((byte) 0),
    /** Information in the parameters PL_0_3, PL_4_7 and LockTime are used to lock one or more priority level. */
    LOCK((byte) 1);

    private static final Logger logger = LoggerFactory.getLogger(PriorityLevelLock.class);
    private byte code;

    private PriorityLevelLock(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PriorityLevelLock fromCode(byte code) {
        for (PriorityLevelLock testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        logger.error("Invalid code: {}", code);
        return NO_LOCK;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
