package com.booking.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.antlr.v4.runtime.misc.NotNull;


public class ReservationRequest {

	   @NotNull
	    private Long resourceId;
	    private Long userId; // ignored for USER (derived from JWT)
	    private BigDecimal price;
	    private Instant startTime;
	    private Instant endTime;
	    private String status; // optional, validation in service
		public Long getResourceId() {
			return resourceId;
		}
		public void setResourceId(Long resourceId) {
			this.resourceId = resourceId;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
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
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	    
	    
}
