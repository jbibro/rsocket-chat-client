package com.github.jbibro.rsocketchat.client;

import java.io.IOException;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;


public final class ConsoleUtil {
    private ConsoleUtil() { }

    public static void printLine(ConsoleReader console, String author, String message) {
        try {
            CursorBuffer stashed = stashLine(console);
            console.println(author + ">" + message);
            unstashLine(console, stashed);
            console.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CursorBuffer stashLine(ConsoleReader console) {
        CursorBuffer stashed = console.getCursorBuffer().copy();
        try {
            console.getOutput().write("\u001b[1G\u001b[K");
            console.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stashed;
    }


    public static void unstashLine(ConsoleReader console, CursorBuffer stashed) {
        try {
            console.resetPromptLine(console.getPrompt(), stashed.toString(), stashed.cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}