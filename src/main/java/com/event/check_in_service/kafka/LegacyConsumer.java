package com.event.check_in_service.kafka;

import com.event.check_in_service.utility.GlobalConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LegacyConsumer {

    @Value("${legacy.system.url}")
    private String legacyUrl;

    @KafkaListener(topics = GlobalConstants.LEGACY_KAFKA_TOPIC, groupId = GlobalConstants.LEGACY_KAFKA_GROUP_ID,
            concurrency = GlobalConstants.LEGACY_KAFKA_CONCURRENCY)
    public void getMessageForLegacySystem(String data) {

        log.info("Attempting to received message for legacy system: {}", data);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(legacyUrl + "/api/v1/sync", data, String.class);
            log.info("Successfully synced to Legacy System.");
        } catch (Exception e) {
            log.error("Legacy API System failed. Retry in progress.");
            throw e;
        }

    }
}
