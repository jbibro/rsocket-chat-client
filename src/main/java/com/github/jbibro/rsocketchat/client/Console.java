package com.github.jbibro.rsocketchat.client;

import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;

public final class Console {
    private ConsoleReader reader = new ConsoleReader();

    public Console() throws IOException {}

    public String readLine(String prompt) {
        try {
            return reader.readLine(prompt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printLine(String author, String message) {
        try {
            CursorBuffer stashed = stashLine();
            reader.println(author + ">" + message);
            unstashLine(stashed);
            reader.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CursorBuffer stashLine() {
        CursorBuffer stashed = reader.getCursorBuffer().copy();
        try {
            reader.getOutput().write("\u001b[1G\u001b[K");
            reader.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stashed;
    }


    private void unstashLine(CursorBuffer stashed) {
        try {
            reader.resetPromptLine(reader.getPrompt(), stashed.toString(), stashed.cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}