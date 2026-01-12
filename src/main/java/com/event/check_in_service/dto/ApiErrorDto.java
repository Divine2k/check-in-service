package com.event.check_in_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class ApiErrorDto {

    private LocalDateTime timestamp;
    private int errorStatus;
    private String errorName;
    private String message;
}
