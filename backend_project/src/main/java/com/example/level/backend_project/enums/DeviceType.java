package com.example.level.backend_project.enums;

public enum DeviceType {
    THERMOSTAT("thermostat"),
    DIMMER("dimmer"),
    LOCK("lock"),
    SWITCH("switch");

    private final String displayName;

    DeviceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
