package org.openhab.binding.veluxklf200.internal.events;

public interface GroupEventListener extends BaseEventListener<GroupEvent> {
    public int getListenedGroupId();
}
