package com.booking.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.booking.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

	   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(optional=false)
	    @JoinColumn(name = "resource_id")
	    private Resource resource;

	    @ManyToOne(optional=false)
	    @JoinColumn(name = "user_id")
	    private User user;

	    @Enumerated(EnumType.STRING)
	    private ReservationStatus status = ReservationStatus.PENDING;

	    @Column(precision = 12, scale = 2)
	    private BigDecimal price;

	    private Instant startTime;
	    private Instant endTime;

	    private Instant createdAt;
	    private Instant updatedAt;
	    
	    @PrePersist
	    void prePersist() { 
	    	  createdAt = Instant.now(); 
	    	  updatedAt = createdAt; 
	    	}

	    @PreUpdate
	    void preUpdate() { 
	    	  updatedAt = Instant.now(); 
	    	}
	    
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Resource getResource() {
			return resource;
		}
		public void setResource(Resource resource) {
			this.resource = resource;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public ReservationStatus getStatus() {
			return status;
		}
		public void setStatus(ReservationStatus status) {
			this.status = status;
		}
		public java.math.BigDecimal getPrice() {
			return price;
		}
		public void setPrice(java.math.BigDecimal price) {
			this.price = price;
		}
		public Instant getStartTime() {
			return startTime;
		}
		public void setStartTime(Instant startTime) {
			this.startTime = startTime;
		}
		public Instant getEndTime() {
			return endTime;
		}
		public void setEndTime(Instant endTime) {
			this.endTime = endTime;
		}
		public Instant getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(Instant createdAt) {
			this.createdAt = createdAt;
		}
		public Instant getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
		}
	    
	    
}
