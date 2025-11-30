package org.example.core.allure;

import io.qameta.allure.Step;

public class Steps {

    @Step("{message}")
    public static void step(String message) {
        Attach.text("", message);
    }

    @Step("{message}")
    public static void step(String message, Runnable runnable) {
        runnable.run();
    }
}
