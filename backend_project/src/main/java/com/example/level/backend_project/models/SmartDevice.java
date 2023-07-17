package com.example.level.backend_project.models;

import com.example.level.backend_project.enums.Model;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;
@Builder
@Data
public class SmartDevice {
    private UUID deviceUUID;
    private UUID spaceUUID;
    private Model model;
    private String deviceName;
    private String deviceSerialNumber;
    private Instant createdAt;
    private Instant lastUpdatedAt;
    private Instant lastConfiguredAt;
    private boolean isPaired;
    private UUID pairedHubUUID;

    public SmartDevice (UUID deviceUUID,
                        UUID spaceUUID,
                        Model model,
                        String deviceName,
                        String deviceSerialNumber,
                        Instant createdAt,
                        Instant lastUpdatedAt,
                        Instant lastConfiguredAt,
                        boolean isPaired,
                        UUID pairedHubUUID) {
        // we need to check those required parameters are not null
        Preconditions.checkNotNull(deviceUUID);
        Preconditions.checkNotNull(spaceUUID);
        Preconditions.checkNotNull(model);
        Preconditions.checkNotNull(deviceName);
        Preconditions.checkNotNull(deviceSerialNumber);
        this.deviceUUID = deviceUUID;
        this.spaceUUID = spaceUUID;
        this.model = model;
        this.deviceName = deviceName;
        this.deviceSerialNumber = deviceSerialNumber;
        this.isPaired = isPaired;
        this.pairedHubUUID = pairedHubUUID;

        if (createdAt == null) {
            createdAt = Instant.now();
        } else {
            this.createdAt = createdAt;
        }
        if (lastUpdatedAt == null) {
            lastUpdatedAt = Instant.now();
        } else {
            this.lastUpdatedAt = lastUpdatedAt;
        }
        if (lastConfiguredAt == null) {
            lastConfiguredAt = Instant.now();
        } else {
            this.lastConfiguredAt = lastConfiguredAt;
        }


    }
}
