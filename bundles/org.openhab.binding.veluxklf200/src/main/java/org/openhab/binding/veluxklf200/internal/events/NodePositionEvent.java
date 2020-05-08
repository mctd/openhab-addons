package org.openhab.binding.veluxklf200.internal.events;

import org.openhab.binding.veluxklf200.internal.commands.status.ExecutionState;
import org.openhab.binding.veluxklf200.internal.commands.status.Position;

public interface NodePositionEvent extends NodeEvent {
    public ExecutionState getExecutionState();

    public Position getCurrentPosition();

    public Position getTarget();

    public Position getFp1currentPosition();

    public Position getFp2currentPosition();

    public Position getFp3currentPosition();

    public Position getFp4currentPosition();

    public int getRemainingTime();

    public int getTimeStamp();
}
