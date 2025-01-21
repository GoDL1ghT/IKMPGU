package ru.godl1ght.entity;

public enum TaskStatus {
    NEW("Новая"),
    IN_PROGRESS("В работе"),
    COMPLETED("Завершена");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}