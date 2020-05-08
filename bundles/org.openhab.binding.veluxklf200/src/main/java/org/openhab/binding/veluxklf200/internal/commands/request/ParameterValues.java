package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.veluxklf200.internal.commands.status.NodeParameter;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;

public class ParameterValues {
    private Position[] positions = new Position[17];

    public ParameterValues() {
    }

    public ParameterValues setPosition(NodeParameter parameter, Position position) {
        this.positions[parameter.getCode()] = position;
        return this;
    }

    /**
     * Indicates if a specific Functional Parameter value is set.
     *
     * @param fpIndex
     * @return
     */
    public boolean isParameterSet(int fpIndex) {
        if (this.positions[fpIndex] != null) {
            return true;
        }
        return false;
    }

    public @Nullable Position[] getPositions() {
        return this.positions;
    }

    /*
     * public short[] toShortArray() {
     * short[] arr = new short[17];
     *
     * for (int i = 0; i < this.positions.length; i++) {
     * arr[i] = this.positions[i] != null ? this.positions[i].getCode() : 0;
     * }
     *
     * return arr;
     * }
     */
}
