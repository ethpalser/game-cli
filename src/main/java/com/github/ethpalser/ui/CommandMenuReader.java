package com.github.ethpalser.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class CommandMenuReader {

    private static final String READER_CLOSED_ERROR_MESSAGE = "reader closed";
    private static final String INPUT_INVALID_MESSAGE = "invalid input";

    private final Set<String> escapeCommands;
    private final BufferedReader br;
    private boolean canRead;
    private BufferedWriter bw;
    private boolean canWrite;

    public CommandMenuReader(final BufferedReader ioReader) {
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

    public String readLine() throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }
        return this.br.readLine();
    }

    public String readLine(String regex) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        String response;
        boolean matchesRegEx;
        do {
            response = this.br.readLine();
            if (this.getEscapeCommands().contains(response.toLowerCase(Locale.ROOT))) {
                return response;
            }

            matchesRegEx = response.matches(regex);
            if (!matchesRegEx) {
                printErrorMessage(INPUT_INVALID_MESSAGE);
            }
        } while (matchesRegEx);
        return response;
    }

    public int readChoice(int choiceMin, int choiceMax) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }
        int choice = 0;
        boolean isValidChoice = false;
        do {
            String response = this.br.readLine();
            if (this.getEscapeCommands().contains(response.toLowerCase(Locale.ROOT))) {
                return -1;
            }

            try {
                choice = Integer.parseInt(response);
            } catch (NumberFormatException ex) {
                printErrorMessage(INPUT_INVALID_MESSAGE);
                continue;
            }

            isValidChoice = choiceMin <= choice && choice <= choiceMax;
            if (!isValidChoice) {
                printErrorMessage(INPUT_INVALID_MESSAGE);
            }
        } while (!isValidChoice);
        return choice;
    }

    public int readChoice(int choiceMax) throws IOException {
        return this.readChoice(0, choiceMax);
    }

    public void close() throws IOException {
        this.br.close();
        this.canRead = false;
        if (this.bw != null) {
            this.bw.close();
            this.canWrite = false;
        }
    }

    public String readOption(String[] options) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            String input = this.br.readLine();
            if (this.getEscapeCommands().contains(input.toLowerCase(Locale.ROOT))) {
                return null;
            }

            String[] args = this.getArgs(input);
            int index = this.getOptionIndex(input);
            if (0 <= index && index < options.length) {
                return options[index] + " " + String.join(" ", args);
            }

            String option = getFromOptions(input, options);
            if (option != null) {
                return option + " " + String.join(" ", args);
            }
            this.printErrorMessage(INPUT_INVALID_MESSAGE);
        } while (true);
    }

    public String readCommand(String[] options, String regex) throws IOException {
        if (!this.canRead) {
            throw new IOException(READER_CLOSED_ERROR_MESSAGE);
        }

        do {
            String input = this.br.readLine();
            if (this.getEscapeCommands().contains(input.toLowerCase(Locale.ROOT))) {
                return null;
            }

            if (input.matches(regex)) {
                return input;
            }

            String[] args = this.getArgs(input);
            String option = getFromOptions(input, options);
            if (option != null) {
                return option + " " + String.join(" ", args);
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
            return Integer.parseInt(toCheck);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String getFromOptions(String input, String[] options) {
        String option = input.split("\\s")[0].toLowerCase(Locale.ROOT);
        for (String o : options) {
            if (o.equals(option)) {
                return option;
            }
        }
        return null;
    }


}
