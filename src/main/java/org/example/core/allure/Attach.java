package org.example.core.allure;

import io.qameta.allure.Allure;

public class Attach {

    public static void text(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

    public static void json(String name, String content) {
        Allure.addAttachment(name, "application/json", content);
    }
    
    public static void html(String name, String content) {
        Allure.addAttachment(name, "text/html", content);
    }
}
