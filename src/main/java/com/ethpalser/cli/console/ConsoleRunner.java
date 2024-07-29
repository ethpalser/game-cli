package com.ethpalser.cli.console;

import com.ethpalser.cli.menu.Context;
import com.ethpalser.cli.menu.Menu;
import com.ethpalser.cli.menu.MenuItem;
import com.ethpalser.cli.menu.event.Event;
import com.ethpalser.cli.menu.event.EventType;
import com.ethpalser.cli.menu.event.Result;
import com.ethpalser.cli.menu.exception.InvalidContextException;
import java.io.IOException;
import java.util.List;

public class ConsoleRunner {

    private final Context context;

    public ConsoleRunner(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        this.context = context;
    }

    public ConsoleRunner(Context context, Menu main) {
        this(context);
        this.context.reset();
        this.context.setDefault(main);
    }

    private String awaitInput(ConsoleReader reader) {
        if (reader == null) {
            return null;
        }
        try {
            List<String> visibleOptions = this.context.peek().getChildren().values()
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
     * Perform one cycle of sending events and receiving input. This may be used to perform finer control over the
     * runtime by specifying when a cycle should be performed. This method will never close the reader and writer
     * provided to it, as such you should close these separately
     *
     * @param reader ConsoleReader that handles IO reads
     * @param writer ConsoleWriter that handles IO writes
     * @return boolean false if an escape is registered, otherwise true
     */
    public boolean runCycle(ConsoleReader reader, ConsoleWriter writer) throws IllegalArgumentException, IOException,
            InvalidContextException {
        if (reader == null || writer == null) {
            throw new IllegalArgumentException("i/o objects are null");
        }
        if (!reader.ready() || !writer.ready()) {
            throw new IOException("i/o streams closed");
        }
        Menu activeMenu = this.context.peek();
        if (activeMenu == null) {
            throw new InvalidContextException();
        }

        this.sendEvent(new Event(EventType.PRE_RENDER), activeMenu);
        this.awaitOutput(writer, activeMenu.getTextDisplay());
        this.sendEvent(new Event(EventType.RENDER), activeMenu);
        this.sendEvent(new Event(EventType.POST_RENDER), activeMenu);

        String input = this.awaitInput(reader);
        if (input == null || reader.getEscapeCommands().contains(input)) {
            return false;
        }

        if (reader.getBackCommands().contains(input)) {
            this.context.pop();
        } else {
            // Split the input, as it may have arguments and would not match a menu option
            String[] split = input.split("\\s+");
            MenuItem selected = activeMenu.getChild(split[0]);
            if (selected != null && !selected.isDisabled()) {
                this.sendEvent(new Event(EventType.EXECUTE, input), selected);
            }
        }
        return true;
    }

    /**
     * Runs main loop to call runCycle. Closes reader and writer after an escape command is registered in runCycle.
     * Running open
     *
     * @param reader ConsoleReader that handles IO reads
     * @param writer ConsoleWriter that handles IO writes
     */
    public void open(ConsoleReader reader, ConsoleWriter writer) {
        boolean canRun = reader.ready() && writer.ready() && this.context.getDefault() != null;
        while (canRun) {
            try {
                canRun = this.runCycle(reader, writer);
            } catch (InvalidContextException ex) {
                System.err.println(ex.getMessage());
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
        try {
            reader.close();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
