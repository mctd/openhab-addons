package org.openhab.binding.veluxklf200.internal.events;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class EventBroker {
    private static final Logger logger = LoggerFactory.getLogger(EventBroker.class);
    private static Set<BaseEventListener<?>> listeners = new HashSet<BaseEventListener<?>>();

    private EventBroker() {

    }

    public static void addListener(BaseEventListener<?> listener) {
        logger.debug("Adding listener {} for brdigeId: {}", listener, listener.getBridgeUID());

        listeners.add(listener);
    }

    public static void removeListener(BaseEventListener<?> listener) {
        logger.debug("Removing listener {} for brdigeId: {}", listener, listener.getBridgeUID());

        listeners.remove(listener);
    }

    public static void notifyEvent(BaseEvent event) {
        int notifiedListeners = 0;

        for (BaseEventListener<?> listener : listeners) {
            if (!listener.getBridgeUID().equals(event.getBridgeUID())) {
                continue;
            }

            if (event instanceof NodeEvent) {
                NodeEvent nodeEvent = (NodeEvent) event;
                if (listener instanceof NodeEventListener) {
                    NodeEventListener nodeListener = (NodeEventListener) listener;

                    // Ensure event is for the correct Bridge and NodeId
                    if (nodeListener.getListenedNodeId() == nodeEvent.getNodeId()) {
                        nodeListener.handleEvent(nodeEvent);
                        notifiedListeners++;
                    }
                }
            }

            if (event instanceof GroupEvent) {
                GroupEvent groupEvent = (GroupEvent) event;
                if (listener instanceof GroupEventListener) {
                    GroupEventListener groupListener = (GroupEventListener) listener;

                    if (groupEvent.getGroupId() == groupListener.getListenedGroupId()) {
                        groupListener.handleEvent(groupEvent);
                        notifiedListeners++;
                    }
                }
            }

            if (event instanceof BridgeEvent) {
                BridgeEvent bridgeEvent = (BridgeEvent) event;
                if (listener instanceof BridgeEventListener) {
                    BridgeEventListener bridgeListener = (BridgeEventListener) listener;
                    bridgeListener.handleEvent(bridgeEvent);
                    notifiedListeners++;
                }
            }

        }

        logger.trace("Notified {} listener(s) for event {} on bridge: {}", notifiedListeners, event,
                event.getBridgeUID());
    }
}
