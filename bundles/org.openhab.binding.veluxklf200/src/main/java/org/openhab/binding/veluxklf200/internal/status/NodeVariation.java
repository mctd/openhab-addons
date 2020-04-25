package org.openhab.binding.veluxklf200.internal.status;

public enum NodeVariation {
    /** Not set. */
    NOT_SET((byte) 0, "Not set"),
    /** Window is a top hung window. */
    TOPHUNG((byte) 1, "Window is a top hung window"),
    /** Window is a kip window. */
    KIP((byte) 2, "Window is a kip window"),
    /** Window is a flat roof. */
    FLAT_ROOF((byte) 3, "Window is a flat roof"),
    /** Window is a sky light. */
    SKY_LIGHT((byte) 4, "Window is a sky light");

    private byte code;
    private String description;

    NodeVariation(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static NodeVariation fromCode(byte code) {
        for (NodeVariation testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        return null; // TODO: throw exception ?
    }

    @Override
    public String toString() {
        return this.description;
    }
}
