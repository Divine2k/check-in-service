package com.event.check_in_service.kafka;

import com.event.check_in_service.utility.ApplicationException;
import com.event.check_in_service.utility.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AttendancePublisher {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public void syncKafkaProducer(String topicName, String payload)  {

        try {

            log.info("Sending the payload to kafka topic: {}", topicName);
            kafkaTemplate.send(topicName, payload).get(GlobalConstants.KAFKA_TIMEOUT, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new ApplicationException("Kafka error for topic: "+ topicName, e);
        }
    }

}
