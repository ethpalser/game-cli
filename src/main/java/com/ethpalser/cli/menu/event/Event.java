package com.ethpalser.cli.menu.event;

public class Event {

    private final EventType eventType;
    private final String commandString;

    public Event(EventType eventType, String commandString) {
        this.eventType = eventType;
        this.commandString = commandString;
    }

    public Event(EventType eventType) {
        this(eventType, null);
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getCommandString() {
        return commandString;
    }
}
