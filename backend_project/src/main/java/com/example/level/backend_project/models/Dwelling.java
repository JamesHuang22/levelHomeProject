package com.example.level.backend_project.models;

import com.example.level.backend_project.models.SmartDevice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Dwelling {
    private UUID spaceUUID;
    private String name;
    private boolean isOccupied;
    private Collection<Hub> installedHubs;
}