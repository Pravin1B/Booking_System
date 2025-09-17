package com.booking.dto;

import jakarta.validation.constraints.NotBlank;

public class ResourceRequest {

	    @NotBlank
	    private String name;
	    private String type;
	    private String description;
	    private Integer capacity;
	    private Boolean active;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Integer getCapacity() {
			return capacity;
		}
		public void setCapacity(Integer capacity) {
			this.capacity = capacity;
		}
		public Boolean getActive() {
			return active;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
	    
	    
}
