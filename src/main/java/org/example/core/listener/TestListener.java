package org.example.core.listener;

import io.qameta.allure.Allure;
import org.example.core.allure.Attach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class TestListener implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        Allure.step("⏸️ Тест отключен: " + context.getTestMethod());
        reason.ifPresent(r -> Attach.text("Причина отключения", r));
        TestWatcher.super.testDisabled(context, reason);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Allure.step("✅ Тест успешно завершён: " + context.getTestMethod());
        Attach.text("Результат теста", "УСПЕХ");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        Allure.step("⚠️ Тест прерван: " + context.getTestMethod());
        if (cause != null) {
            Attach.text("Причина прерывания", cause.getMessage());
            Attach.text("Стек вызовов", getStackTrace(cause));
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Allure.step("❌ Тест упал: " + context.getTestMethod());
        if (cause != null) {
            Attach.text("Причина ошибки", cause.getMessage());
            Attach.text("Стек вызовов", getStackTrace(cause));
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Allure.step("▶️ Запуск теста: " + context.getTestMethod());
        Attach.text("Имя теста", context.getDisplayName());
        Attach.text("Класс теста", context.getTestClass().map(Class::getName).orElse("Неизвестно"));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Allure.step("⏹ Завершение теста: " + context.getTestMethod());
    }
    
    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
