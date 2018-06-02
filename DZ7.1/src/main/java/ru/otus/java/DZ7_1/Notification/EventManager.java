package ru.otus.java.DZ7_1.Notification;

import ru.otus.java.DZ7_1.Devices.ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    Map<EventsEnum, List<EventListener>> listeners = new HashMap<>();

    public EventManager(EventsEnum... eventTypes) {
        for (EventsEnum eventType : eventTypes) {
            listeners.put(eventType, new ArrayList<>());
        }
    }

    public void subscribe(EventsEnum eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void notify(ATM atm, EventsEnum eventType, Map<Integer, Integer> value) {
        List<EventListener> users = listeners.get(eventType);
        for (EventListener listener : users) {
            listener.notify(atm, eventType, value);
        }
    }
}
