package com.booking.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.booking.dto.ReservationRequest;
import com.booking.entity.Reservation;
import com.booking.enums.ReservationStatus;
import com.booking.service.ReservationService;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;
    public ReservationController(ReservationService service) { 
    	this.service = service; 
    	}

    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public Page<Reservation> list(@RequestParam Optional<ReservationStatus> status,
                                  @RequestParam Optional<BigDecimal> minPrice,
                                  @RequestParam Optional<BigDecimal> maxPrice,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "createdAt,desc") String sort) {
        var parts = sort.split(",");
        var dir = parts.length > 1 && "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
        boolean restrictToUser = !isAdmin();
        return service.list(status, minPrice, maxPrice, page, size, Sort.by(dir, parts[0]), restrictToUser);
    }

    
    @GetMapping("/{id}")
    public Reservation get(@PathVariable Long id) {
        boolean restrictToOwner = !isAdmin();
        return service.get(id, restrictToOwner);
    }

    
    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody ReservationRequest req) {
        boolean admin = isAdmin();
        Reservation r = service.create(req, admin);
        return ResponseEntity.status(201).body(r);
    }

    
    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @Valid @RequestBody ReservationRequest req) {
        boolean admin = isAdmin();
        return service.update(id, req, admin);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean admin = isAdmin();
        service.delete(id, admin);
        return ResponseEntity.noContent().build();
    }
}

