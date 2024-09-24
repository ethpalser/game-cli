package com.ethpalser.cli.console;

import com.ethpalser.cli.util.Pair;
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
    private static final String INPUT_INVALID_MESSAGE = "input invalid";
    private static final String INPUT_NULL_MESSAGE = "input unexpected, received nothing";

    private final Set<String> escapeCommands;
    private final Set<String> backCommands;
    private final Set<String> helpCommands;
    private final BufferedReader br;
    private boolean canRead;
    private BufferedWriter bw;
    private boolean canWrite;

    public ConsoleReader(final BufferedReader ioReader) {
        this.br = ioReader;
        this.canRead = true;
        this.canWrite = false;
        this.escapeCommands = Set.of("exit", "close", "quit");
        this.backCommands = Set.of("back", "previous", "prev");
        this.helpCommands = Set.of("help");
    }

    public ConsoleReader(final BufferedReader ioReader, final BufferedWriter ioWriter) {
        this(ioReader);
        this.bw = ioWriter;
        this.canWrite = ioWriter != null;
    }

    public Set<String> getEscapeCommands() {
        return this.escapeCommands;
    }

    public Set<String> getBackCommands() {
        return this.backCommands;
    }

    public Set<String> getHelpCommands() {
        return this.helpCommands;
    }

    private boolean matchesReservedCommand(String input) {
        String lowerInput = input.toLowerCase(Locale.ROOT);
        return this.getHelpCommands().contains(lowerInput)
                || this.getBackCommands().contains(lowerInput)
                || this.getEscapeCommands().contains(lowerInput);
    }

    /**
     * Reads an input from a Reader. This requires that the input is a valid option or a special command, such as:
     * escape commands, back commands, help commands, etc. This option can be the index of an option or its name
     * from the list of options. The result will be the option in addition to any following text to work as a command.
     *
     * @param options List of options to select from
     * @return String representing the selected option and any following text
     * @throws IOException An I/O exception occurred with the Reader or Writer.
     */
    public Pair<String, String[]> readOption(List<String> options) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            this.printPrefixLine(READER_PREFIX);
            String input = this.br.readLine();
            if (input == null) {
                this.printErrorMessage(INPUT_NULL_MESSAGE);
                return new Pair<>("exit", null);
            }

            Pair<String, String[]> parts = this.getParts(input);
            if (this.matchesReservedCommand(parts.getFirst().toLowerCase(Locale.ROOT))) {
                return parts;
            }

            String option;
            int index = this.getOptionIndex(parts.getFirst());
            if (0 <= index && index < options.size()) {
                option = options.get(index);
            } else {
                option = this.getFromOptions(parts.getFirst(), options);
            }

            if (option != null) {
                // reserved commands override options
                if (this.matchesReservedCommand(option.toLowerCase(Locale.ROOT))) {
                    return new Pair<>(parts.getFirst().toLowerCase(), parts.getLast());
                }
                return parts;
            }
            this.printErrorMessage(INPUT_INVALID_MESSAGE);
        } while (true);
    }

    /**
     * Reads an input from the Reader. This input must match the regular expression, an option or a special command,
     * such as: escape commands, back commands, help commands, etc. The option can its name, but not its index (use
     * readOption to use an option's index). Matching the regular expression takes priority over the option, and
     * if the option fails to match the regular expression an error may occur handling the option based on its
     * contract.
     *
     * @param options List of options to select from
     * @param regex   String representing the requirements of an option to use it (i.e. name, flags, values, number
     *                of arguments).
     * @return String representing the selected option and any following text
     * @throws IOException An I/O exception occurred with the Reader or Writer.
     */
    public Pair<String, String[]> readCommand(List<String> options, String regex) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            this.printPrefixLine(READER_PREFIX);
            String input = this.br.readLine();
            if (input == null) {
                this.printErrorMessage(INPUT_NULL_MESSAGE);
                return new Pair<>("exit", null);
            }

            Pair<String, String[]> parts = this.getParts(input);
            if (input.matches(regex)) {
                return parts;
            }

            if (this.getEscapeCommands().contains(parts.getFirst())) {
                return new Pair<>("exit", null);
            }

            String option = this.getFromOptions(parts.getFirst(), options);
            if (option != null) {
                return parts;
            }
            this.printErrorMessage(INPUT_INVALID_MESSAGE);
        } while (true);
    }

    public boolean ready() {
        return this.canRead;
    }

    public void close() throws IOException {
        this.canRead = false;
        this.br.close();
        if (this.bw != null) {
            this.canWrite = false;
            this.bw.close();
        }
    }

    private void printErrorMessage(String message) throws IOException {
        if (this.bw != null && this.canWrite) {
            this.bw.write(message);
            this.bw.flush();
        } else {
            System.err.println(message);
        }
    }

    private void printPrefixLine(String prefix) throws IOException {
        if (this.bw != null && this.canWrite) {
            this.bw.write("\n" + prefix);
            this.bw.flush();
        }
    }

    private boolean isEmpty(String input) {
        return input == null || input.replace("\\s", "").length() == 0;
    }

    private Pair<String, String[]> getParts(String input) {
        String regex = "\\s+";
        if (this.isEmpty(input)) {
            return new Pair<>("", new String[]{}); // No args as there is it is null, or is only the option / command name
        }
        String[] arr = input.split(regex);
        if (arr.length > 1) {
            return new Pair<>(arr[0], Arrays.copyOfRange(arr, 1, arr.length));
        }
        return new Pair<>(arr[0], null);
    }

    private int getOptionIndex(String command) {
        if (command == null) {
            return -1;
        }
        try {
            return Integer.parseInt(command) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String getFromOptions(String command, List<String> options) {
        for (String o : options) {
            if (o.equalsIgnoreCase(command)) {
                return o;
            }
        }
        return null;
    }


}
