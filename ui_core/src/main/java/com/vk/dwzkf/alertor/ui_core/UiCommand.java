package com.vk.dwzkf.alertor.ui_core;

import java.util.function.Supplier;

public interface UiCommand {
    void execute();
    String name();

    static UiCommand from(String name, Runnable action) {
        return new UiCommand() {
            @Override
            public void execute() {
                action.run();
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    static UiCommand from(Supplier<String> nameSupplier, Runnable action) {
        return new UiCommand() {
            @Override
            public void execute() {
                action.run();
            }

            @Override
            public String name() {
                return nameSupplier.get();
            }
        };
    }
}
