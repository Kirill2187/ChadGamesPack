package com.chadgames.gamespack.utils;

import java.util.HashMap;

public class EventSource {
    public interface Observer {
        void trigger();
    }

    private final HashMap<String, Observer> observers = new HashMap<>();

    public void triggerEvent() {
        for (Observer observer : observers.values()) {
            observer.trigger();
        }
    }

    public void subscribe(Observer observer, String tag) {
        observers.put(tag, observer);
    }

    public void unsubscribe(String tag) {
        observers.remove(tag);
    }
}
