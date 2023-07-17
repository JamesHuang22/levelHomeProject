package com.example.level.backend_project.enums;


import com.google.common.collect.ImmutableList;
import java.util.Collection;

public enum Model {
    THERMOSTAT(DeviceType.THERMOSTAT, "thermostat", ImmutableList.of(
            CommandName.SET_TEMPERATURE,
            CommandName.SET_THERMOSTAT_MODE)),
    SWITCH(DeviceType.SWITCH, "switch", ImmutableList.of(
            CommandName.SET_SWITCH_STATE)),
    DIMMER(DeviceType.DIMMER, "dimmer", ImmutableList.of(
            CommandName.SET_DIMMER_LEVEL)),
    LOCK(DeviceType.LOCK, "lock", ImmutableList.of(
            CommandName.SET_LOCK_STATE));

    private final DeviceType deviceType;
    private final String displayName;
    private final Collection<CommandName> allowedCommands;

    Model(DeviceType deviceType, String displayName, Collection<CommandName> allowedCommands) {
        this.deviceType = deviceType;
        this.displayName = displayName;
        this.allowedCommands = allowedCommands;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
    public String getDisplayName() {
        return displayName;
    }
    public Collection<CommandName> getAllowedCommands() {
        return allowedCommands;
    }

    public boolean isCommandAllowed(CommandName commandName) {
        return allowedCommands.contains(commandName);
    }
}
