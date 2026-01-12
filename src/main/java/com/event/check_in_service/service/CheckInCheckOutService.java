package com.event.check_in_service.service;

import com.event.check_in_service.domain.Attendance;

public interface CheckInCheckOutService {

    Attendance processAttendance(String employeeId);
}
