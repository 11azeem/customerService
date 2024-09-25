package com.hms.customerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Customer Service", description = "Customer Service APIs")
@RestController
@RequestMapping("/api/v1")
public class MainRestController {
    public static final String LOGIN_MESSAGE = "Welcome to Suite Spott. Have a wonderful Stay!!!";

    @Autowired
    private CredentialRepository credentialRepository;

    @GetMapping("/register")
    public ResponseEntity<CredentialResponse> register() {
        Credential credential = new Credential();
        credential.setId(UUID.randomUUID());
        credential.setPassword(RandomStringGenerator.generateRandomString(12));
        credentialRepository.save(credential);

        //producer.pubSocialEvent_1("LOGIN", credential.getId());
        CredentialResponse credentialResponse = new CredentialResponse(LOGIN_MESSAGE, credential);
        return ResponseEntity.ok(credentialResponse);
    }
}
