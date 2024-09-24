package com.ethpalser.cli.console;

import com.ethpalser.cli.util.Pair;
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
    public Pair<String, String[]> readOption(List<String> options) {
        if (!canRead) {
            return null;
        }
        Pair<String, String[]> resp = switch (readCount) {
            case 0 -> new Pair<>("-1", null);
            case 1 -> new Pair<>(options.get(0), null); // Assuming input was "1"
            case 2 -> new Pair<>("test", new String[]{"-flag", "text"});
            case 3 -> new Pair<>("back", null);
            default -> new Pair<>("exit", new String[]{"-y"});
        };
        this.readCount++;
        return resp;
    }

    @Override
    public Pair<String, String[]> readCommand(List<String> options, String regex) {
        if (!canRead) {
            return null;
        }
        Pair<String, String[]> resp = switch (readCount) {
            case 0 -> new Pair<>("-1", null);
            case 1 -> new Pair<>(options.get(0), null); // Assuming input was "1"
            case 2 -> new Pair<>("test", new String[]{"-flag", "text"});
            case 3 -> new Pair<>("back", null);
            default -> new Pair<>("exit", new String[]{"-y"});
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
