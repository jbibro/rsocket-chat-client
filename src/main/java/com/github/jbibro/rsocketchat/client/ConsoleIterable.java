package com.github.jbibro.rsocketchat.client;

import java.io.IOException;
import java.util.Iterator;

import jline.console.ConsoleReader;


public class ConsoleIterable implements Iterable<String>, Iterator<String> {
    private ConsoleReader console;
    private String prompt;
    private String lastLine;

    ConsoleIterable(ConsoleReader console, String prompt) {
        this.console = console;
        this.prompt = prompt;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        try {
            lastLine = console.readLine(prompt);
            return lastLine != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String next() {
        return lastLine;
    }
}