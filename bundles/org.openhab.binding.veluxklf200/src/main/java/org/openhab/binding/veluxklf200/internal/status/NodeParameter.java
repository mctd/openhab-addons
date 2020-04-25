package org.openhab.binding.veluxklf200.internal.status;

public enum NodeParameter {
    /** Main Parameter. */
    MP((byte) 0x00, "Main Parameter"),
    /** Functional Parameter number 1. */
    FP1((byte) 0x01, "Functional Parameter number 1"),
    /** Functional Parameter number 2. */
    FP2((byte) 0x02, "Functional Parameter number 2"),
    /** Functional Parameter number 3. */
    FP3((byte) 0x03, "Functional Parameter number 3"),
    /** Functional Parameter number 4. */
    FP4((byte) 0x04, "Functional Parameter number 4"),
    /** Functional Parameter number 5. */
    FP5((byte) 0x05, "Functional Parameter number 5"),
    /** Functional Parameter number 6. */
    FP6((byte) 0x06, "Functional Parameter number 6"),
    /** Functional Parameter number 7. */
    FP7((byte) 0x07, "Functional Parameter number 7"),
    /** Functional Parameter number 8. */
    FP8((byte) 0x08, "Functional Parameter number 8"),
    /** Functional Parameter number 9. */
    FP9((byte) 0x09, "Functional Parameter number 9"),
    /** Functional Parameter number 10. */
    FP10((byte) 0x0A, "Functional Parameter number 10"),
    /** Functional Parameter number 11. */
    FP11((byte) 0x0B, "Functional Parameter number 11"),
    /** Functional Parameter number 12. */
    FP12((byte) 0x0C, "Functional Parameter number 12"),
    /** Functional Parameter number 13. */
    FP13((byte) 0x0D, "Functional Parameter number 13"),
    /** Functional Parameter number 14. */
    FP14((byte) 0x0E, "Functional Parameter number 14"),
    /** Functional Parameter number 15. */
    FP15((byte) 0x0F, "Functional Parameter number 15"),
    /** Functional Parameter number 16. */
    FP16((byte) 0x10, "Functional Parameter number 16"),
    /** Not used. */
    NOT_USED((byte) 0xFF, "Not used");

    private byte code;
    private String description;

    NodeParameter(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return this.code;
    }

    public static NodeParameter fromCode(byte code) {
        for (NodeParameter testValue : values()) {
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
