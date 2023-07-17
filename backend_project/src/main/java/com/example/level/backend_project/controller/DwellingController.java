package com.example.level.backend_project.controller;

import com.example.level.backend_project.models.Dwelling;
import com.example.level.backend_project.models.Hub;
import com.example.level.backend_project.repository.DwellingRepository;
import com.example.level.backend_project.repository.HubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/dwellings")
public class DwellingController {
    private static final Logger logger = LoggerFactory.getLogger(DwellingController.class);
    private final DwellingRepository dwellingRepository;
    private final HubRepository hubRepository;

    @Autowired
    public DwellingController(DwellingRepository dwellingRepository, HubRepository hubRepository) {
        this.dwellingRepository = dwellingRepository;
        this.hubRepository = hubRepository;
    }

    @PutMapping("/{dwellingUUID}/occupied")
    public ResponseEntity<Dwelling> setOccupied(@PathVariable UUID dwellingUUID) {
        return setDwellingOccupancyStatus(dwellingUUID, true);
    }

    @PutMapping("/{dwellingUUID}/vacant")
    public ResponseEntity<Dwelling> setDwellingVacant(@PathVariable UUID dwellingUUID) {
        return setDwellingOccupancyStatus(dwellingUUID, false);
    }

    @PostMapping("/{dwellingUUID}/hub/{hubUUID}")
    public ResponseEntity<Dwelling> installHub(@PathVariable UUID dwellingUUID, @PathVariable UUID hubUUID) {
        try {
            Dwelling dwelling = dwellingRepository.findById(dwellingUUID).orElse(null);
            Hub hub = hubRepository.findById(hubUUID).orElse(null);
            if (dwelling == null || hub == null) {
                logger.error("Dwelling with UUID {} or hub with UUID = {} does not exist in DB. Failed to install.", dwellingUUID, hubUUID);
                return ResponseEntity.notFound().build();
            }
            dwelling.getInstalledHubs().add(hub);
            // we also need to set the spaceUUID of the dwelling to the hub, to ensure the data integrity
            hub.setSpaceUUID(dwellingUUID);
            dwellingRepository.save(dwelling);
            hubRepository.save(hub);
            return ResponseEntity.ok(dwelling);
        } catch (Exception e) {
            logger.error("Error while installing hub with UUID {} to dwelling with UUID {}.", hubUUID, dwellingUUID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Dwelling>> listAllDwelling () {
        try {
            List<Dwelling> dwellings = dwellingRepository.findAll();
            return ResponseEntity.ok(dwellings);
        } catch (Exception e) {
            logger.error("Error while fetching all dwellings.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // helper function for setting the occupancy status of a dwelling
    private ResponseEntity<Dwelling> setDwellingOccupancyStatus(UUID dwellingUuid, boolean isOccupied) {
        try {
            Dwelling dwelling = dwellingRepository.findById(dwellingUuid).orElse(null);
            if (dwelling == null) {
                logger.error("Dwelling with UUID {} does not exist in DB", dwellingUuid);
                return ResponseEntity.notFound().build();
            }
            dwelling.setOccupied(isOccupied);
            dwellingRepository.save(dwelling);
            return ResponseEntity.ok(dwelling);
        } catch (Exception e) {
            logger.error("Error while updating dwelling occupancy status for UUID {}.", dwellingUuid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
