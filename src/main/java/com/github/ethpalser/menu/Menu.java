package com.github.ethpalser.menu;

import com.github.ethpalser.menu.event.Event;
import java.util.Map;

public interface Menu {

    String getName();

    void handleEvent(Event event);

    Map<String, Menu> getChildren();

    void addChildren(Menu... children);

    default void addChild(Menu child) {
        addChildren(child);
    }

    void removeChildren(String... children);

    default void removeChild(String name) {
        removeChildren(name);
    }

}
