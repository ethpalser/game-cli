package com.ethpalser.cli.menu.event;

public class Event {

    private final EventType eventType;
    private final String command;

    public Event(EventType eventType, String command) {
        this.eventType = eventType;
        this.command = command;
    }

    public Event(EventType eventType) {
        this(eventType, null);
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getCommand() {
        return command;
    }
}
