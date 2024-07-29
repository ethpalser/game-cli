package com.ethpalser.cli.menu.exception;

public class InvalidContextException extends Exception {

    public InvalidContextException() {
        super("invalid context state");
    }
}
