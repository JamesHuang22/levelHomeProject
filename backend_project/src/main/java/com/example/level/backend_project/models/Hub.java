package com.example.level.backend_project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Hub {
    private UUID deviceUUID;
    private UUID spaceUUID;
    private String deviceName;
    private String deviceSerialNumber;
    private Collection<SmartDevice> pairedDevices;
}
