package com.hms.customerservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "credential", schema = "customerschema")
public class Credential {
    @Id
    @Column(name = "customerid", nullable = false)
    private UUID id;

    @Size(max = 50)
    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}