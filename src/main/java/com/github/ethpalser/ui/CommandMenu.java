package com.github.ethpalser.ui;

import com.github.ethpalser.menu.Menu;
import com.github.ethpalser.menu.event.Event;
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
            this.active.handleEvent(Event.PRE_RENDER, null);
            do {
                this.active.handleEvent(Event.RENDER, null);
                this.active.handleEvent(Event.POST_RENDER, null);
                // todo: await input from io
                String input = "";
                if (this.getEscapeCommands().contains(input)) {
                    close = true;
                    break;
                }
                Menu selected = this.active.getChildren().get("");
                if (this.active.getName().equals(selected.getName())) {
                    this.active.handleEvent(Event.SELECT, null);
                    this.active = selected;
                    this.activeUpdated = true;
                }
                selected.handleEvent(Event.EXECUTE, null);
            } while (!this.activeUpdated);
        } while (!close);
        // todo: clean up io
    }

    @Override
    public void run() {
        this.open();
    }
}
