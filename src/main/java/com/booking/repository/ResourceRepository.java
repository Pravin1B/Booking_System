package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
