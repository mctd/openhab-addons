package org.openhab.binding.veluxklf200.internal.handler;

import org.openhab.binding.veluxklf200.internal.status.NodeTypeSubType;
import org.openhab.binding.veluxklf200.internal.status.Position;

public interface ActuatorListener {
    void PositionChanged(Position newPosition);

    void InfoUpdated(NodeTypeSubType nodeTypeSubType);
}
