package com.hms.customerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hms.customerservice.kafka.KafkaProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Customer Service", description = "Customer Service APIs")
@RestController
@RequestMapping("/api/v1")
public class MainRestController {
    public static final String LOGIN_MESSAGE = "Welcome to Suite Spott. Have a wonderful Stay!!!";

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("/register")
    public ResponseEntity<CredentialResponse> register() throws JsonProcessingException {
        Credential credential = new Credential();
        credential.setId(UUID.randomUUID());
        credential.setPassword(RandomStringGenerator.generateRandomString(12));
        credentialRepository.save(credential);

        //kafkaProducer.pubRegisterEvent(credential.getId());
        CredentialResponse credentialResponse = new CredentialResponse(LOGIN_MESSAGE, credential);
        return ResponseEntity.ok(credentialResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String customerId = loginRequest.getCustomerId();
        String password = loginRequest.getPassword();
        String message;

        Optional<Credential> credentialFromDb = credentialRepository.findById(UUID.fromString(customerId));
        if (credentialFromDb.isPresent() && credentialFromDb.get().getPassword().trim().equals(password)) {
            if (StringUtils.isNotEmpty(credentialFromDb.get().getName())) {
                message = "Welcome " + credentialFromDb.get().getName() + " to Suite Spott. Have a wonderful Stay!!!";
            } else {
                message = "Welcome to Suite Spott. Have a wonderful Stay!!!";
            }

            return ResponseEntity.ok()
                    .header("Authorization", RandomStringGenerator.generateRandomString(20))
                    .body(message);
        } else {
            return ResponseEntity
                    .ok()
                    .body("Invalid Credentials. Please Try Again!");
        }
    }

    @PostMapping("/updateDetails")
    public ResponseEntity<String> updateDetails(@RequestBody LoginRequest loginRequest) {

        String customerId = loginRequest.getCustomerId();
        String password = loginRequest.getPassword();
        String name = loginRequest.getName();

        if (customerId != null && !customerId.isEmpty() && name != null && !name.isEmpty()) {
            Credential credential = new Credential();
            credential.setId(UUID.fromString(customerId));
            credential.setName(name);
            credential.setPassword(password);
            credentialRepository.save(credential);
        } else {
            return ResponseEntity.badRequest().body("Invalid Request");
        }

        return ResponseEntity
                .ok()
                .body("Details updated for " + name + ". Thank you for choosing Suite Spott");

    }
}
