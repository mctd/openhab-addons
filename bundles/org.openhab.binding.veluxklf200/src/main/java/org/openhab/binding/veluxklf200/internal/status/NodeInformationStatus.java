package org.openhab.binding.veluxklf200.internal.status;

public enum NodeInformationStatus {
    /** OK - Request accepted. */
    OK((byte) 0, "OK - Request accepted"),
    /** Error – Request rejected. */
    ERROR_REJECTED((byte) 1, "Error – Request rejected"),
    /** Error – Invalid node index. */
    ERROR_INVALID_INDEX((byte) 2, "Error – Invalid node index"),
    /** Reserved. */
    RESERVED((byte) 0xFF, "Reserved");

    private byte code;
    private String description;

    NodeInformationStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static NodeInformationStatus fromCode(byte code) {
        for (NodeInformationStatus testValue : values()) {
            if (testValue.getCode() == code) {
                return testValue;
            }
        }

        // Reserved values are 5 to 255
        return RESERVED;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
