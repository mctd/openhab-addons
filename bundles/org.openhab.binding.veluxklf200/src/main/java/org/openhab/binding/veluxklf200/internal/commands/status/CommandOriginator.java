package org.openhab.binding.veluxklf200.internal.commands.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public enum CommandOriginator {
    /** Unknown origin */
    UNKNOWN((byte) 0),
    /** User Remote control causing action on actuator */
    USER((byte) 1),
    /** Rain sensor */
    RAIN((byte) 2),
    /** Timer controlled */
    TIMER((byte) 3),
    /** UPS unit */
    UPS((byte) 5),
    /** Stand Alone Automatic Controls */
    SAAC((byte) 8),
    /** Wind sensor */
    WIND((byte) 9),
    /** Managers for requiring a particular electric load shed */
    LOAD_SHEDDING((byte) 11),
    /** Local light sensor */
    LOCAL_LIGHT((byte) 12),
    /**
     * Used in context with commands transmitted on basis of an unknown sensor for protection of an end-product or house
     * goods
     */
    UNSPECIFIC_ENVIRONMENT_SENSOR((byte) 13),
    /** Used in context with emergency or security commands */
    EMERGENCY((byte) 255);

    private byte status;
    private final static Logger logger = LoggerFactory.getLogger(CommandOriginator.class);

    private CommandOriginator(byte status) {
        this.status = status;
    }

    public byte getCode() {
        return this.status;
    }

    public static @Nullable CommandOriginator fromCode(byte code) {
        for (CommandOriginator testCommand : values()) {
            if (testCommand.getCode() == code) {
                return testCommand;
            }
        }

        logger.error("Invalid code: {}", code);
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
