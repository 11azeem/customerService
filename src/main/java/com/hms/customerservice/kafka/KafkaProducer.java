package com.hms.customerservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "customer-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        kafkaTemplate.getProducerFactory().updateConfigs(Collections.singletonMap(
                ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "1000"
        ));
    }

    public void pubRegisterEvent(UUID customerId) throws JsonProcessingException {
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent();
        userRegisteredEvent.setEventType("USER_REGISTERED");
        userRegisteredEvent.setCustomerId(String.valueOf(customerId));

        // convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String datum = objectMapper.writeValueAsString(userRegisteredEvent);

        logger.info("Producing message -> {}", datum);

        CompletableFuture<SendResult<String, String>> future =
                this.kafkaTemplate.send(TOPIC, "customer-key-1", datum);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message=[{}] with offset=[{}]", datum, result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send message=[{}] due to : {}", datum, ex.getMessage());
                if (ex instanceof KafkaException) {
                    logger.error("KafkaException details: ", ex);
                }
                if (ex.getCause() != null) {
                    logger.error("Root cause: ", ex.getCause());
                }
            }
        });
    }
}
