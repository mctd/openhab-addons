package org.openhab.binding.veluxklf200.internal.events;

public interface NodeEventListener extends BaseEventListener<NodeEvent> {
    public int getListenedNodeId();

}
