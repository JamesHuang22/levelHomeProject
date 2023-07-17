package com.example.level.backend_project.repository;

import com.example.level.backend_project.models.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// As we extend the Jpa Repository, it will provide us with generate CRUD operations.
public interface HubRepository extends JpaRepository<Hub, UUID> {
}
