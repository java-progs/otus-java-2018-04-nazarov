package ru.otus.java.DZ7_1.Notification;

import ru.otus.java.DZ7_1.Devices.ATM;

import java.util.Map;

public interface EventListener {
    public void notify(ATM atm, EventsEnum eventType, Map<Integer, Integer> value);
}
