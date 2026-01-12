package com.event.check_in_service.serviceImpl;

import com.event.check_in_service.domain.Attendance;
import com.event.check_in_service.domain.Event;
import com.event.check_in_service.dto.KafkaPayload;
import com.event.check_in_service.enums.Published;
import com.event.check_in_service.enums.Status;
import com.event.check_in_service.repository.AttendanceRepository;
import com.event.check_in_service.repository.EventRepository;
import com.event.check_in_service.service.CheckInCheckOutService;
import com.event.check_in_service.utility.ApplicationException;
import com.event.check_in_service.utility.GlobalConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class CheckInCheckOutServiceImpl implements CheckInCheckOutService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EventRepository eventRepository;

    @Transactional
    public Attendance processAttendance(String employeeId) {

        try {

        Optional<Attendance> lastestRecord = attendanceRepository.findFirstByEmployeeIdOrderByCreatedAtDesc(employeeId);
        if (lastestRecord.isPresent() && isWithinTimeDuration(lastestRecord.get().getCreatedAt())) {
            log.warn("Duplicated Tap detected for employeeId: {}", employeeId);
            return lastestRecord.get();
        }

        Optional<Attendance> getExistingRecord = attendanceRepository.findByEmployeeIdAndStatus(employeeId, Status.OPEN);
        if (getExistingRecord.isEmpty())
            return processCheckIn(employeeId);
        else
            return processCheckOut(getExistingRecord.get());

    } catch (Exception e) {
            throw new ApplicationException("Error occurred in ServiceImpl for employeeID: " + employeeId, e);
        }
    }

    private Attendance processCheckIn(String employeeId) {

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus(Status.OPEN);

        return attendanceRepository.save(attendance);
    }

    private Attendance processCheckOut(Attendance existingRecord) {

        if (existingRecord.getCheckInTime() == null)
            throw new RuntimeException("Cannot process check-out without check-in");

        try {

            existingRecord.setCheckOutTime(LocalDateTime.now());
            existingRecord.setStatus(Status.CLOSED);

            long totalDurationInMin = Duration.between(existingRecord.getCheckInTime(),
                    existingRecord.getCheckOutTime()).toMinutes();

            existingRecord.setTotalDuration((double) (totalDurationInMin));
            Attendance updatedRecord = attendanceRepository.save(existingRecord);
            saveEventInDb(existingRecord);

            return updatedRecord;

        } catch (Exception e) {
            throw new RuntimeException("Database error during check-out. Full error message: ", e);
        }
    }

    private boolean isWithinTimeDuration(LocalDateTime lastModified) {

        if (lastModified == null)
            return false;
        double seconds = Duration.between(lastModified, LocalDateTime.now()).toSeconds();

        return seconds < GlobalConstants.GRACE_TIME_DURATION;
    }

    private void saveEventInDb(Attendance existingRecord) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            KafkaPayload payload = KafkaPayload.builder()
                    .employeeId(existingRecord.getEmployeeId())
                    .totalWorkingHours(existingRecord.getTotalDuration())
                    .timeStamp(LocalDateTime.now().toString())
                    .build();

            String json = objectMapper.writeValueAsString(payload);

            Event event = Event.builder()
                    .payload(json)
                    .status(Published.PENDING)
                    .build();

            eventRepository.save(event);

        } catch (Exception e) {
            throw new RuntimeException("Error in saving data to EvenDB: ", e);
        }
    }



















}
