package com.ethpalser.cli.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ConsoleReader {

    private static final String READER_PREFIX = "> ";
    private static final String READER_CLOSED_ERROR_MESSAGE = "reader closed";
    private static final String INPUT_INVALID_MESSAGE = "invalid input";

    private final Set<String> escapeCommands;
    private final BufferedReader br;
    private boolean canRead;
    private BufferedWriter bw;
    private boolean canWrite;

    public ConsoleReader(final BufferedReader ioReader) {
        this.br = ioReader;
        this.canRead = true;
        this.canWrite = false;
        this.escapeCommands = Set.of("exit", "close", "quit", "q");
    }

    public BufferedReader getBufferedReader() {
        return this.br;
    }

    protected Set<String> getEscapeCommands() {
        return this.escapeCommands;
    }

    public void setErrorWriter(BufferedWriter ioWriter) {
        if (this.bw != null) {
            try {
                this.bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.bw = ioWriter;
        this.canWrite = true;
    }

    public void printErrorMessage(String message) throws IOException {
        if (this.bw != null && this.canWrite) {
            this.bw.write(message);
            this.bw.flush();
        }
    }

    public void printPrefixLine(String prefix) throws IOException {
        if (this.bw != null && this.canWrite) {
            this.bw.write("\n" + prefix);
            this.bw.flush();
        }
    }

    public void close() throws IOException {
        this.br.close();
        this.canRead = false;
        if (this.bw != null) {
            this.bw.close();
            this.canWrite = false;
        }
    }

    public String readOption(List<String> options) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            this.printPrefixLine(READER_PREFIX);
            String input = this.br.readLine();
            if (this.getEscapeCommands().contains(input.toLowerCase(Locale.ROOT))) {
                return input;
            }

            String option;
            int index = this.getOptionIndex(input);
            if (0 <= index && index < options.size()) {
                option = options.get(index);
            } else {
                option = this.getFromOptions(input, options);
            }

            if (option != null) {
                return option + " " + String.join(" ", this.getArgs(input));
            }
            this.printErrorMessage(INPUT_INVALID_MESSAGE);
        } while (true);
    }

    public String readCommand(List<String> options, String regex) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            this.printPrefixLine(READER_PREFIX);
            String input = this.br.readLine();
            if (this.getEscapeCommands().contains(input.toLowerCase(Locale.ROOT))) {
                return input;
            }

            if (input.matches(regex)) {
                return input;
            }

            String option = this.getFromOptions(input, options);
            if (option != null) {
                return option + " " + String.join(" ", this.getArgs(input));
            }
            this.printErrorMessage(INPUT_INVALID_MESSAGE);
        } while (true);
    }

    private boolean isEmpty(String input) {
        return input == null || input.replace("\\s", "").length() != 0;
    }

    private String[] getArgs(String input) {
        String regex = "\\s+";
        if (this.isEmpty(input)) {
            return new String[]{}; // No args as there is it is null, or is only the option / command name
        }
        String[] arr = input.split(regex);
        if (arr.length > 1) {
            return Arrays.copyOfRange(arr, 1, arr.length - 1);
        }
        return new String[]{};
    }

    private int getOptionIndex(String input) {
        if (input == null) {
            return -1;
        }
        String toCheck;
        if (getArgs(input).length != 0) {
            toCheck = input.split("\\s+")[0];
        } else {
            toCheck = input;
        }
        try {
            return Integer.parseInt(toCheck) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String getFromOptions(String input, List<String> options) {
        String option = input.split("\\s")[0].toLowerCase(Locale.ROOT);
        for (String o : options) {
            if (o.equals(option)) {
                return o;
            }
        }
        return null;
    }


}
