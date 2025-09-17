package com.booking.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

     @Query("select count(r) from Reservation r "
     		+ "where r.resource.id = :resourceId and r.status = 'CONFIRMED' and "
     		+ "r.startTime < :end and r.endTime > :start")
   
     long countOverlappingConfirmed(
    		@Param("resourceId") Long resourceId, 
    		@Param("start") Instant start, 
    		@Param("end") Instant end
    		);
}
