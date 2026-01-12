package com.event.check_in_service.domain;

import com.event.check_in_service.enums.Published;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Published status;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;

    private int retryCount = 0;
    private String lastKafkaError;

    @Column(name = "created_At")
    private LocalDateTime createdAt;

    @PrePersist
    void getTimeForCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null)
            this.status = Published.PENDING;
    }

}
