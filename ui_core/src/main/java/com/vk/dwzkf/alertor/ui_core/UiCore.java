package com.vk.dwzkf.alertor.ui_core;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UiCore<R> {
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String nextString() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer readInt() {
        return Integer.parseInt(Objects.requireNonNull(nextString()));
    }

    public static Long readLong() {
        return Long.parseLong(Objects.requireNonNull(nextString()));
    }

    private static final String DELIMITER = "-".repeat(20);
    private final List<UiCommand> commands = new ArrayList<>();
    private final AtomicBoolean terminated = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);

    private final UiCommand exit = UiCommand.from("exit", this::end);
    @Getter
    private R result;

    public UiCore() {
    }

    public void addCommand(UiCommand command) {
        if (!commands.contains(command)) {
            commands.add(command);
        }
    }

    public abstract void configure();
    public void onTerminate() {
        //noop
    };

    public void addCommands(List<UiCommand> commands) {
        commands.forEach(this::addCommand);
    }

    public UiCore<R> start() {
        if (terminated.get()) return this;
        if (started.get()) return this;
        started.set(true);
        configure();
        while (!terminated.get()) {
            mainLoop();
        }
        onTerminate();
        return this;
    }

    public void end() {
        terminated.set(true);
    }

    private void mainLoop() {
        System.out.println(DELIMITER);
        printCommand(0, exit);
        int idx = 1;
        for (UiCommand command : commands) {
            printCommand(idx++, command);
        }
        System.out.print("Choose: ");
        try {
            int index = UiCore.readInt() - 1;
            if (index == -1) {
                exit.execute();
            } else {
                try {
                    if (index >= commands.size()) {
                        System.out.println("Bad command.");
                    } else {
                        commands.get(index).execute();
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Error on command: %s, %s", index, e.getMessage()));
                }
            }
        } catch (Exception e ) {
            System.out.println("Bad input.");
        }
    }

    private void printCommand(int idx, UiCommand uiCommand) {
        System.out.println(String.format("%s. %s", idx, uiCommand.name()));
    }
}
