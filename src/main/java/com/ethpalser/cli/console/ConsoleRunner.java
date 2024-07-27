package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.event.Event;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.event.Result;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

public class ConsoleRunner implements Runnable {

    private final Context context;
    private Menu main;

    public ConsoleRunner(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.context = context;
    }

    public ConsoleRunner(Context context, Menu main) {
        this(context);
        this.main = main;
        this.context.push(this.main);
    }

    private Menu getActive() {
        return this.context.peek();
    }

    private void setActive(Menu menu) {
        this.context.push(menu);
    }

    private Set<String> getBackCommands() {
        return Set.of("back", "previous", "prev");
    }

    private Set<String> getEscapeCommands() {
        return Set.of("exit", "close", "quit");
    }

    private String awaitInput(ConsoleReader reader) {
        if (reader == null) {
            return null;
        }
        try {
            List<String> visibleOptions = this.getActive().getChildren().values()
                    .stream().filter(child -> !child.isHidden()).map(MenuItem::getName).toList();
            return reader.readOption(visibleOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void awaitOutput(ConsoleWriter writer, String message) {
        if (writer == null) {
            return;
        }
        try {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(Event event, MenuItem receiver) {
        Result result = receiver.receiveEvent(event);
        if (result.hasError()) {
            System.err.println(result.getMessage());
        }
    }

    /**
     * Establish open streams and run main loop.
     */
    public void open(ConsoleReader reader, ConsoleWriter writer) {
        boolean close = false;
        do {
            if (this.getActive() == null) {
                this.setActive(this.main);
            }
            do {
                this.sendEvent(new Event(EventType.PRE_RENDER), this.getActive());
                this.awaitOutput(writer, this.getActive().getTextDisplay());
                this.sendEvent(new Event(EventType.RENDER), this.getActive());
                this.sendEvent(new Event(EventType.POST_RENDER), this.getActive());

                String input = this.awaitInput(reader);
                if (input == null || this.getEscapeCommands().contains(input)) {
                    close = true;
                    break;
                }

                if (this.getBackCommands().contains(input)) {
                    this.context.pop();
                } else {
                    // Split the input, as it may have arguments and would not match a menu option
                    String[] split = input.split("\\s+");
                    MenuItem selected = this.getActive().getChild(split[0]);
                    if (selected != null && !selected.isDisabled()) {
                        this.sendEvent(new Event(EventType.EXECUTE, input), selected);
                    }
                }
            } while (!this.context.refresh());
        } while (!close);

        try {
            reader.close();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        ConsoleReader reader = new ConsoleReader(new BufferedReader(new InputStreamReader(System.in)));
        ConsoleWriter writer = new ConsoleWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        reader.setErrorWriter(writer.getBufferedWriter());
        this.open(reader, writer);
    }
}
