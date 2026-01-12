package com.event.check_in_service.kafka;

import com.event.check_in_service.utility.GlobalConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createLegacyTopic() {
        return TopicBuilder.name(GlobalConstants.LEGACY_KAFKA_TOPIC)
                .partitions(GlobalConstants.LEGACY_KAFKA_PARTITIONS)
                .replicas(GlobalConstants.LEGACY_KAFKA_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic createEmailNotificationTopic() {
        return TopicBuilder.name(GlobalConstants.EMAIL_KAFKA_TOPIC)
                .partitions(GlobalConstants.EMAIL_KAFKA_PARTITIONS)
                .replicas(GlobalConstants.EMAIL_KAFKA_REPLICAS)
                .build();
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        FixedBackOff off = new FixedBackOff(GlobalConstants.KAFKA_ERROR_INTERVAL, GlobalConstants.MAX_KAFKA_ATTEMPTS);
        DeadLetterPublishingRecoverer dlpr = new DeadLetterPublishingRecoverer(kafkaTemplate);
        return new DefaultErrorHandler(dlpr, off);
    }
}
