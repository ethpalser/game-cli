package com.ethpalser.cli.console;

import java.util.List;

public class MockConsoleReader extends ConsoleReader {

    private int readCount;
    private boolean canRead;

    public MockConsoleReader() {
        super(null, null);
        this.readCount = 0;
        this.canRead = true;
    }

    @Override
    public String readOption(List<String> options) {
        if (!canRead) {
            return null;
        }
        String resp = switch (readCount) {
            case 0 -> "-1";
            case 1 -> options.get(0); // Assuming input was "1"
            case 2 -> "test -flag text";
            case 3 -> "back";
            default -> "exit";
        };
        this.readCount++;
        return resp;
    }

    @Override
    public String readCommand(List<String> options, String regex) {
        if (!canRead) {
            return null;
        }
        String resp = switch (readCount) {
            case 0 -> "-1";
            case 1 -> options.get(0); // Assuming input was "1"
            case 2 -> "test -flag text";
            case 3 -> "back";
            default -> "exit";
        };
        this.readCount++;
        return resp;
    }

    @Override
    public boolean ready() {
        return this.canRead;
    }

    @Override
    public void close() {
        this.canRead = false;
    }

}
