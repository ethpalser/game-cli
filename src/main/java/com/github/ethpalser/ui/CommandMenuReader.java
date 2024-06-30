package com.github.ethpalser.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class CommandMenuReader {

    private final static String READER_CLOSED_ERROR_MESSAGE = "reader closed";
    private final static String INPUT_INVALID_MESSAGE = "invalid input";

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

    private Set<String> getEscapeCommands() {
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

}
