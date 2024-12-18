package com.monitoring.comunication.repository;

import com.monitoring.comunication.entity.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admins, UUID> {
}
