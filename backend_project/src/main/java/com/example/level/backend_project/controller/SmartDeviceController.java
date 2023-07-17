package com.example.level.backend_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.level.backend_project.models.SmartDevice;
import com.example.level.backend_project.repository.SmartDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// This controller will handle all the operations against the smartDevice

@RestController
@RequestMapping("/api/v1/smartDevices")

public class SmartDeviceController {
    private static final Logger logger = LoggerFactory.getLogger(SmartDeviceController.class);

    private final SmartDeviceRepository smartDeviceRepository;

    @Autowired
    public SmartDeviceController(SmartDeviceRepository smartDeviceRepository) {
        this.smartDeviceRepository = smartDeviceRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<SmartDevice> createNewSmartDevice(@RequestBody SmartDevice smartDevice) {
        try {
            SmartDevice savedSmartDevice = smartDeviceRepository.save(smartDevice);
            return new ResponseEntity<>(savedSmartDevice, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{smartDeviceUuid}")
    public ResponseEntity<String> deleteSmartDevice(@PathVariable UUID smartDeviceUUID) {
        try {
            SmartDevice smartDevice = smartDeviceRepository.findById(smartDeviceUUID).orElse(null);
            if (smartDevice == null) {
                logger.error("Smart device with UUID {} does not exist in the DB.", smartDeviceUUID);
                return ResponseEntity.notFound().build();
            }
            // we only delete a device that is not currently paired with the hub
            // if it is paired, we will log an error and return 400
            if (!smartDevice.isPaired()) {
                logger.info("Smart device with UUID {} is not paired with any hub. Deleting it.", smartDeviceUUID);
                smartDeviceRepository.deleteById(smartDeviceUUID);
                return ResponseEntity.ok().build();
            } else {
                logger.error("Smart device with UUID {} is paired with a hub. Cannot delete it.", smartDeviceUUID);
                return ResponseEntity.status(400).body("Can not delete paired device");
            }
        } catch (DataAccessException exception) {
            logger.error("Error while deleting smart device with UUID {} from DB.", smartDeviceUUID);
            return ResponseEntity.status(500).body("Error while deleting smart device");
        }
    }

    @GetMapping("/get/{smartDeviceUuid}")
    public ResponseEntity<SmartDevice> getSmartDevice(@PathVariable UUID smartDeviceUUID) {
        try {
            return smartDeviceRepository.findById(smartDeviceUUID).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DataAccessException exception) {
            logger.error("Error while getting smart device with UUID {} from DB.", smartDeviceUUID);
            return ResponseEntity.status(500).build();
        }
    }

    // This endpoint support update following infos for device: deviceName, spaceUUID, deviceSerialNumber, pairedHubUUID
    @PutMapping("/update/{smartDeviceUuid}")
    public ResponseEntity<SmartDevice> updateSmartDevice(@PathVariable UUID deviceUUID, @RequestBody SmartDevice deviceUpdates) {
        try {
            return smartDeviceRepository.findById(deviceUUID)
                    .map(device -> {
                        if (deviceUpdates.getDeviceName() != null) {
                            logger.info("Updating device name from {} to {}.", device.getDeviceName(), deviceUpdates.getDeviceName());
                            device.setDeviceName(deviceUpdates.getDeviceName());
                        }
                        if (deviceUpdates.getSpaceUUID() != null) {
                            logger.info("Updating device's space UUID from {} to {}.", device.getSpaceUUID(), deviceUpdates.getSpaceUUID());
                            device.setSpaceUUID(deviceUpdates.getSpaceUUID());
                        }
                        if (deviceUpdates.getDeviceSerialNumber() != null) {
                            logger.info("Updating device's serial number from {} to {}.", device.getDeviceSerialNumber(), deviceUpdates.getDeviceSerialNumber());
                            device.setDeviceSerialNumber(deviceUpdates.getDeviceSerialNumber());
                        }
                        if (deviceUpdates.getPairedHubUUID() != null) {
                            logger.info("Updating device's paired hub UUID from {} to {}.", device.getPairedHubUUID(), deviceUpdates.getPairedHubUUID());
                            device.setPairedHubUUID(deviceUpdates.getPairedHubUUID());
                        }
                        return ResponseEntity.ok(smartDeviceRepository.save(device));
                    }).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DataAccessException exception) {
            logger.error("Error while updating smart device with UUID {} from DB.", deviceUUID);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/listAllSmartDevices")
    public ResponseEntity<List<SmartDevice>> listAllSmartDevices() {
        try {
            return ResponseEntity.ok(smartDeviceRepository.findAll());
        } catch (DataAccessException exception) {
            logger.error("Error while retrieving all smart devices from DB.");
            return ResponseEntity.status(500).build();
        }
    }
}
