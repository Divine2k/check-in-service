package com.event.check_in_service.dto;

import com.event.check_in_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {

    private Long id;
    private String employeeId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Status status;
    private LocalDateTime createdAt;
    private Double totalDuration;

}
