package com.event.check_in_service.controller;

import com.event.check_in_service.domain.Attendance;
import com.event.check_in_service.service.CheckInCheckOutService;
import com.event.check_in_service.serviceImpl.CheckInCheckOutServiceImpl;
import com.event.check_in_service.utility.GlobalConstants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(GlobalConstants.REQUEST_MAPPING_POINT)
public class CheckInCheckOutController {

    @Autowired
    CheckInCheckOutService checkInCheckOutService;

    @PostMapping(GlobalConstants.CONTROLLER_POST_MAPPING)
    @Operation(summary = "Process employee check-in / check-out")
    public ResponseEntity<Attendance> checkInOrOut(@RequestParam String employeeId) {

            Attendance attendance = checkInCheckOutService.processAttendance(employeeId);
            return new ResponseEntity<>(attendance, HttpStatus.OK);
    }
}
