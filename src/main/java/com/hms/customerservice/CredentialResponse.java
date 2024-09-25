package com.hms.customerservice;

public class CredentialResponse {
    private Credential credential;
    private String message;

    // Constructor
    public CredentialResponse(String message, Credential credential) {
        this.credential = credential;
        this.message = message;
    }

    // Getters and setters
    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

