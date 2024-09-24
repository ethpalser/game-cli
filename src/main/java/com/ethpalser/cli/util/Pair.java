package com.ethpalser.cli.util;

public class Pair<T, U> {

    private final T first;
    private final U last;

    public Pair(T first, U last) {
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public U getLast() {
        return last;
    }
}
