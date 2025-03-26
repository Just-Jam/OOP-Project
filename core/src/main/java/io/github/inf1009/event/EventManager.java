package io.github.inf1009.event;

import java.util.ArrayList;

public class EventManager {
    private static EventManager instance;
    private ArrayList<iEventListener> listeners = new ArrayList<iEventListener>();

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void addListener(iEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(iEventListener listener) {
        listeners.remove(listener);
    }

    public void postEvent(GameEvent event) {
        for (iEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
