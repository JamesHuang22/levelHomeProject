package com.example.level.backend_project.repository;

import com.example.level.backend_project.enums.Model;
import com.example.level.backend_project.models.SmartDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;


// As we extend the Jpa Repository, it will provide us with generate CRUD operations.
// Added some custom query methods for later use in the service layer.
public interface SmartDeviceRepository extends JpaRepository<SmartDevice, UUID> {
    @Query("SELECT sd FROM SmartDevice sd WHERE sd.spaceUUID = :spaceUUID")
    Collection<SmartDevice> findAllBySpaceUUID(@Param("spaceUUID") UUID spaceUUID);

    @Query("SELECT sd FROM SmartDevice sd WHERE sd.spaceUUID = :spaceUUID and sd.model = :model")
    Collection<SmartDevice> findAllByDeviceModelAndSpaceUUID(@Param("spaceUUID") UUID spaceUUID, @Param("model") Model model);
}
