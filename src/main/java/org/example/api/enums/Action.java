package org.example.api.enums;

import lombok.Getter;

@Getter
public enum Action {
    LOGIN("LOGIN"),
    ACTION("ACTION"),
    LOGOUT("LOGOUT");

    private final String value;

    Action(String value) {
        this.value = value;
    }
}
