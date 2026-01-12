package com.event.check_in_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaPayload {

    private String employeeId;
    private Double totalWorkingHours;
    private String timeStamp;
}
