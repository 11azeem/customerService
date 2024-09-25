package com.hms.customerservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {
}
