package com.example.level.backend_project.enums;

public enum CommandName {
    SET_TEMPERATURE("setTemperature"),
    SET_THERMOSTAT_MODE("setThermostatMode"),
    SET_SWITCH_STATE("setSwitchState"),
    SET_DIMMER_LEVEL("setDimmerLevel"),
    SET_LOCK_STATE("setLockState");
    private final String value;

    CommandName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
