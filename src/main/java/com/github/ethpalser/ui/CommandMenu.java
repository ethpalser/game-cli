package com.github.ethpalser.ui;

import com.github.ethpalser.menu.Menu;
import com.github.ethpalser.menu.MenuItem;
import com.github.ethpalser.menu.event.Event;
import com.github.ethpalser.menu.event.EventType;
import java.util.Set;

public class CommandMenu implements Runnable {

    private final Menu main;
    private Menu active;
    private boolean activeUpdated;

    public CommandMenu(Menu main) {
        this.main = main;
        this.active = main;
        this.activeUpdated = false;
    }

    private void setActiveMenu(final Menu menu) {
        this.active = menu;
        this.activeUpdated = true;
    }

    private Set<String> getEscapeCommands() {
        return Set.of("exit", "close", "quit", "q");
    }

    /**
     * Establish open streams and run main loop.
     */
    public void open() {
        // todo: set up io
        boolean close = false;
        do {
            if (this.active == null) {
                this.setActiveMenu(this.main);
            }
            this.active.receiveEvent(new Event(EventType.PRE_RENDER));
            do {
                this.active.receiveEvent(new Event(EventType.RENDER));
                this.active.receiveEvent(new Event(EventType.POST_RENDER));
                // todo: await input from io
                String input = "";
                if (this.getEscapeCommands().contains(input)) {
                    close = true;
                    break;
                }
                MenuItem selected = this.active.getChildren().get("");
                if (this.active.getName().equals(selected.getName())) {
                    this.active.receiveEvent(new Event(EventType.SELECT));
                    if (selected instanceof Menu) {
                        this.active = (Menu) selected;
                        this.activeUpdated = true;
                    }
                }
                this.active.receiveEvent(new Event(EventType.EXECUTE, input));
            } while (!this.activeUpdated);
        } while (!close);
        // todo: clean up io
    }

    @Override
    public void run() {
        this.open();
    }
}
