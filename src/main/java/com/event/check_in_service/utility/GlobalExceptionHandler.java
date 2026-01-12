package com.event.check_in_service.utility;

import com.event.check_in_service.dto.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorDto> genericException(Exception e, HttpServletRequest request) {

        log.error("Failed API call {}", e.getMessage());

        ApiErrorDto errorBody = ApiErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorName(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}
