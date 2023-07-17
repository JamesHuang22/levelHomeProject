package com.example.level.backend_project;

import com.example.level.backend_project.controller.SmartDeviceController;
import com.example.level.backend_project.enums.Model;
import com.example.level.backend_project.models.SmartDevice;
import com.example.level.backend_project.repository.SmartDeviceRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmartDeviceControllerTest {

    private SmartDeviceRepository smartDeviceRepository;
    private SmartDeviceController smartDeviceController;

    @BeforeEach
    public void setUp() {
        smartDeviceRepository = mock(SmartDeviceRepository.class);
        smartDeviceController = new SmartDeviceController(smartDeviceRepository);
    }


    @Test
    public void testGetDevice() {
        UUID deviceUuid = UUID.randomUUID();
        String deviceSn = "123456789";
        String deviceName = "test device";
        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, false);
        when(smartDeviceRepository.findById(any())).thenReturn(Optional.of(mockDevice));
        ResponseEntity<SmartDevice> responseEntity  = smartDeviceController.getSmartDevice(deviceUuid);
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getDeviceUUID(), deviceUuid);
    }

    @Test
    public void testCreateSmartDevice() {
        UUID deviceUuid = UUID.randomUUID();
        String deviceSn = "123456789";
        String deviceName = "test device";
        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, false);
        when(smartDeviceRepository.save(any())).thenReturn(mockDevice);
        ResponseEntity<SmartDevice> responseEntity  = smartDeviceController.createNewSmartDevice(mockDevice);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.CREATED.value());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getDeviceUUID(), deviceUuid);
    }

    @Test
    public void testDeletePairedSmartDevice() {
        UUID deviceUuid = UUID.randomUUID();
        String deviceSn = "123456789";
        String deviceName = "test device";
        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, true);
        when(smartDeviceRepository.findById(any())).thenReturn(Optional.of(mockDevice));
        ResponseEntity<String> responseEntity  = smartDeviceController.deleteSmartDevice(deviceUuid);
        assertEquals(responseEntity.getStatusCode().value(), 400);
    }

    @Test
    public void testDeleteUnPairedSmartDevice() {
        UUID deviceUuid = UUID.randomUUID();
        String deviceSn = "123456789";
        String deviceName = "test device";
        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, false);
        when(smartDeviceRepository.findById(any())).thenReturn(Optional.of(mockDevice));
        ResponseEntity<String> responseEntity  = smartDeviceController.deleteSmartDevice(deviceUuid);
        assertEquals(responseEntity.getStatusCode().value(), 200);
    }

    @Test
    public void testUpdateSmartDevice() {
        String deviceSn = "123456789";
        String deviceName = "test device";
        String deviceNewSn = "987654321";
        String deviceNewName = "new test device";
        UUID deviceUuid = UUID.randomUUID();
        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, false);
        SmartDevice updateDevice = createMockDevice(deviceUuid, deviceNewName, deviceNewSn, false);

        when(smartDeviceRepository.findById(any())).thenReturn(Optional.of(mockDevice));
        when(smartDeviceRepository.save(any())).thenReturn(updateDevice);

        ResponseEntity<SmartDevice> responseEntity  = smartDeviceController.updateSmartDevice(deviceUuid, updateDevice);
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testListAllSmartDevices() {
        UUID deviceUuid = UUID.randomUUID();
        UUID deviceUuid2 = UUID.randomUUID();
        String deviceSn = "123456789";
        String deviceSn2 = "987654321";
        String deviceName = "test device";
        String deviceName2 = "test device 2";

        SmartDevice mockDevice = createMockDevice(deviceUuid, deviceName, deviceSn, false);
        SmartDevice mockDevice2 = createMockDevice(deviceUuid2, deviceName2, deviceSn2, true);
        when(smartDeviceRepository.findAll()).thenReturn(ImmutableList.of(mockDevice, mockDevice2));
        ResponseEntity<List<SmartDevice>> responseEntity  = smartDeviceController.listAllSmartDevices();
        assertEquals(responseEntity.getStatusCode().value(), 200);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().iterator().next().getDeviceUUID(), deviceUuid);
    }

    private SmartDevice createMockDevice(UUID deviceUUID,
                                         String deviceName,
                                         String deviceSerialNumber,
                                         boolean isPaired) {
        return SmartDevice.builder()
                .deviceName(deviceName)
                .model(Model.LOCK)
                .deviceSerialNumber(deviceSerialNumber)
                .deviceUUID(deviceUUID)
                .spaceUUID(UUID.randomUUID())
                .createdAt(Instant.now())
                .lastUpdatedAt(Instant.now())
                .lastConfiguredAt(Instant.now())
                .isPaired(isPaired)
                .build();
    }


}
