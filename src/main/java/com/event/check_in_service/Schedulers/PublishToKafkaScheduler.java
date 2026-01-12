package com.event.check_in_service.Schedulers;

import com.event.check_in_service.domain.Event;
import com.event.check_in_service.enums.Published;
import com.event.check_in_service.kafka.AttendancePublisher;
import com.event.check_in_service.repository.EventRepository;
import com.event.check_in_service.utility.GlobalConstants;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PublishToKafkaScheduler {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AttendancePublisher attendancePublisher;

    private boolean isApplicationReady = false;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application is fully ready. Enabling Kafka Scheduler.");
        this.isApplicationReady = true;
    }

    @Scheduled(initialDelay = GlobalConstants.SCHEDULAR_INITIAL_DELAY, fixedDelay = GlobalConstants.SCHEDULAR_FIXED_DELAY)
    @Transactional
    public void payloadTransferScheduler() {

        if (!isApplicationReady) {
            log.info("Waiting for application to be ready...");
            return;
        }
        log.info("Periodic scheduler scan.......");
        List<Event> eventList = eventRepository.findTop10ByStatus(Published.PENDING);

        for (Event e: eventList)
            retryPublishing(e);
    }

    public void retryPublishing(Event e) {

            try {

                attendancePublisher.syncKafkaProducer(GlobalConstants.LEGACY_KAFKA_TOPIC, e.getPayload());
                attendancePublisher.syncKafkaProducer(GlobalConstants.EMAIL_KAFKA_TOPIC, e.getPayload());

                e.setStatus(Published.SENT);
                eventRepository.save(e);
                log.info("Payload for the Event {} has been published to kafka", e.getId());

            } catch (Exception exception) {

                log.warn("Not able to publish payload, retrying in 30 seconds.....");

                int retryCounter= e.getRetryCount() + 1;
                e.setRetryCount(retryCounter);
                e.setLastKafkaError(exception.getMessage());

                if (retryCounter >= GlobalConstants.MAX_KAFKA_PRODUCER_RETRY) {

                    e.setStatus(Published.FAILED);
                    log.error("Event {} failed to publish", e.getId());

                } else {
                    log.warn("Failed attempt to publish Event {}", e.getId());
                }

                eventRepository.save(e);
            }
        }
    }
