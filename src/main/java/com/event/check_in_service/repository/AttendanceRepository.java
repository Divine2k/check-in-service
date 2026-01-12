package com.event.check_in_service.repository;

import com.event.check_in_service.domain.Attendance;
import com.event.check_in_service.enums.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Attendance> findByEmployeeIdAndStatus(String employeeId, Status status);

    Optional<Attendance> findFirstByEmployeeIdOrderByCreatedAtDesc(String employeeId);
}
