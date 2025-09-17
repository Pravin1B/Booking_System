package com.booking.service;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.booking.dto.ReservationRequest;
import com.booking.entity.Reservation;
import com.booking.entity.Resource;
import com.booking.entity.User;
import com.booking.enums.ReservationStatus;
import com.booking.exception.ForbiddenException;
import com.booking.repository.ReservationRepository;
import com.booking.repository.ResourceRepository;
import com.booking.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repo;
    private final ResourceRepository resourceRepo;
    private final UserRepository userRepo;

    public ReservationService(ReservationRepository repo, ResourceRepository resourceRepo, UserRepository userRepo) {
        this.repo = repo;
        this.resourceRepo = resourceRepo;
        this.userRepo = userRepo;
    }

    public Page<Reservation> list(Optional<ReservationStatus> status,
                                  Optional<BigDecimal> minPrice,
                                  Optional<BigDecimal> maxPrice,
                                  int page,
                                  int size,
                                  Sort sort,
                                  boolean restrictToCurrentUser) {

        Specification<Reservation> spec = (root, query, cb) -> {
            var preds = new java.util.ArrayList<Predicate>();

            status.ifPresent(s -> preds.add(cb.equal(root.get("status"), s)));
            minPrice.ifPresent(min -> preds.add(cb.ge(root.get("price"), min)));
            maxPrice.ifPresent(max -> preds.add(cb.le(root.get("price"), max)));

            if (restrictToCurrentUser) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User u = userRepo.findByUsername(username).orElseThrow();
                preds.add(cb.equal(root.get("user").get("id"), u.getId()));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };

        return repo.findAll(spec, PageRequest.of(page, size, sort));
    }

    public Reservation get(Long id, boolean restrictToOwner) {
        Reservation r = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (restrictToOwner) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!r.getUser().getUsername().equals(username)) {
                throw new SecurityException("Forbidden");
            }
        }
        return r;
    }

    @Transactional
    public Reservation create(ReservationRequest req, boolean isAdmin) {
        Resource res = resourceRepo.findById(req.getResourceId())
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        User user;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!isAdmin) {
            user = userRepo.findByUsername(username).orElseThrow();
        } else {
            if (req.getUserId() != null) {
                user = userRepo.findById(req.getUserId()).orElseThrow();
            } else {
                user = userRepo.findByUsername(username).orElseThrow();
            }
        }

        Reservation r = new Reservation();
        r.setResource(res);
        r.setUser(user);
        r.setPrice(req.getPrice());
        r.setStartTime(req.getStartTime());
        r.setEndTime(req.getEndTime());

        if (req.getStatus() != null && isAdmin) {
            try {
                r.setStatus(ReservationStatus.valueOf(req.getStatus()));
            } catch (IllegalArgumentException ex) {
                r.setStatus(ReservationStatus.PENDING);
            }
        } else {
            r.setStatus(ReservationStatus.PENDING);
        }

        // Overlap prevention for CONFIRMED
        if (r.getStatus() == ReservationStatus.CONFIRMED) {
            if (r.getStartTime() == null || r.getEndTime() == null) {
                throw new IllegalArgumentException("startTime & endTime required for CONFIRMED");
            }
            long overlap = repo.countOverlappingConfirmed(res.getId(), r.getStartTime(), r.getEndTime());
            if (overlap > 0) {
                throw new IllegalStateException("Overlapping confirmed reservation exists");
            }
        }

        return repo.save(r);
    }

    @Transactional
    public Reservation update(Long id, ReservationRequest req, boolean isAdmin) {
        Reservation r = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!isAdmin && !r.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("User Can't Update Status");
        }

        if (req.getResourceId() != null) {
            Resource res = resourceRepo.findById(req.getResourceId()).orElseThrow();
            r.setResource(res);
        }
        if (req.getPrice() != null) {
            r.setPrice(req.getPrice());
        }
        if (req.getStartTime() != null) {
            r.setStartTime(req.getStartTime());
        }
        if (req.getEndTime() != null) {
            r.setEndTime(req.getEndTime());
        }
        if (req.getStatus() != null) {
            ReservationStatus newStatus = ReservationStatus.valueOf(req.getStatus());
            // if changing to CONFIRMED, check overlap
            if (newStatus == ReservationStatus.CONFIRMED) {
                if (r.getStartTime() == null || r.getEndTime() == null) {
                    throw new IllegalArgumentException("startTime & endTime required for CONFIRMED");
                }
                long overlap = repo.countOverlappingConfirmed(r.getResource().getId(), r.getStartTime(), r.getEndTime());
                // If current reservation is already confirmed it will count itself
                if (r.getStatus() != ReservationStatus.CONFIRMED && overlap > 0) {
                    throw new IllegalStateException("Overlapping confirmed reservation exists");
                }
            }
            r.setStatus(newStatus);
        }
        return repo.save(r);
    }

    public void delete(Long id, boolean isAdmin) {
        Reservation r = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!isAdmin && !r.getUser().getUsername().equals(username)) {
            throw new SecurityException("Forbidden");
        }
        repo.deleteById(id);
    }
}
