package com.example.level.backend_project.controller;

import com.example.level.backend_project.models.Hub;
import com.example.level.backend_project.models.SmartDevice;
import com.example.level.backend_project.repository.HubRepository;
import com.example.level.backend_project.repository.SmartDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/hubs")

public class HubController {

    private static final Logger logger = LoggerFactory.getLogger(HubController.class);

    private final HubRepository hubRepository;
    private final SmartDeviceRepository smartDeviceRepository;

    @Autowired
    public HubController(HubRepository hubRepository,
                         SmartDeviceRepository smartDeviceRepository) {
        this.hubRepository = hubRepository;
        this.smartDeviceRepository = smartDeviceRepository;
    }

    @PutMapping("/pair/{hubUUID}/smartDevice/{deviceUUID}")
    public ResponseEntity<Hub> pairSmartDevice (@PathVariable UUID hubUUID, @PathVariable UUID smartDeviceUUID) {
        try {
            Optional<Hub> hubOpt = hubRepository.findById(hubUUID);
            Optional<SmartDevice> deviceOptional = smartDeviceRepository.findById(smartDeviceUUID);
            if (hubOpt.isPresent() && deviceOptional.isPresent()) {
                Hub hub = hubOpt.get();
                SmartDevice smartDevice = deviceOptional.get();
                if (smartDevice.isPaired()) {
                    UUID existingHubUUID = smartDevice.getPairedHubUUID();
                    logger.error("Smart device with UUID {} is already paired with an existing hub {}. Cannot pair it with another hub.",
                            smartDeviceUUID, existingHubUUID);
                    return ResponseEntity.status(400).body(hub);
                } else {
                    smartDevice.setPaired(true);
                    smartDevice.setPairedHubUUID(hubUUID);
                    smartDeviceRepository.save(smartDevice);
                    // add the device to the hub and save to the DB
                    hub.getPairedDevices().add(smartDevice);
                    return ResponseEntity.ok(hubRepository.save(hub));
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            logger.error("Error while pairing smart device with UUID {} to hub with UUID {}.", smartDeviceUUID, hubUUID);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{hubUuid}/deviceState/{deviceUuid}")
    public ResponseEntity<SmartDevice> getDeviceState(@PathVariable UUID hubUUID, @PathVariable UUID deviceUUID) {
        try {
            Optional<Hub> hubOpt = hubRepository.findById(hubUUID);
            Optional<SmartDevice> deviceOpt = smartDeviceRepository.findById(deviceUUID);
            if (hubOpt.isPresent() && deviceOpt.isPresent()) {
                Hub hub = hubOpt.get();
                SmartDevice smartDevice = deviceOpt.get();
                // we check whether this device is paired with the hub
                if (smartDevice.isPaired() && smartDevice.getPairedHubUUID().equals(hub.getDeviceUUID()) && hub.getPairedDevices().contains(smartDevice)) {
                    logger.info("Device with UUID {} is paired with hub with UUID {}", deviceUUID, hubUUID);
                    return ResponseEntity.ok(smartDevice);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            logger.error("Error while getting device state for device with UUID {} paired with hub with UUID {}.", deviceUUID, hubUUID);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{hubUUID}/pairedDevices")
    public ResponseEntity<Collection<SmartDevice>> listPairedDevices(@PathVariable UUID hubUUID) {
        try {
            return hubRepository.findById(hubUUID).map(hub -> ResponseEntity.ok(hub.getPairedDevices())).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception exception) {
            logger.error("Error while getting list of paired devices for hub with UUID {}.", hubUUID);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{hubUUID}/unpair/{deviceUUID}")
    public ResponseEntity<Hub> removeDevice (@PathVariable UUID hubUUID, @PathVariable UUID deviceUUID) {
        try {
            Optional<Hub> hubOpt = hubRepository.findById(hubUUID);
            Optional<SmartDevice> deviceOpt = smartDeviceRepository.findById(deviceUUID);

            if (hubOpt.isPresent() && deviceOpt.isPresent()) {
                Hub hub = hubOpt.get();
                SmartDevice smartDevice = deviceOpt.get();
                if (smartDevice.isPaired() && smartDevice.getPairedHubUUID().equals(hub.getDeviceUUID()) && hub.getPairedDevices().contains(smartDevice)) {
                    smartDevice.setPaired(false);
                    smartDevice.setPairedHubUUID(null);
                    smartDeviceRepository.save(smartDevice);
                    hub.getPairedDevices().remove(smartDevice);
                    return ResponseEntity.ok(hubRepository.save(hub));
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            logger.error("Error while unpairing device with UUID {} from hub with UUID {}.", deviceUUID, hubUUID);
            return ResponseEntity.status(500).build();
        }
    }

}
