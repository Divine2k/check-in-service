package com.event.check_in_service.repository;

import com.event.check_in_service.domain.Event;
import com.event.check_in_service.enums.Published;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findTop10ByStatus(Published status);
}
