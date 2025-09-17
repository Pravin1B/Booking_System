package com.booking.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.booking.dto.ResourceRequest;
import com.booking.entity.Resource;
import com.booking.service.ResourceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService service;
    public ResourceController(ResourceService service) { 
    	 this.service = service; 
    	}

    @GetMapping
    public Page<Resource> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id,asc") String sort) {
    	
        var parts = sort.split(",");
        Sort.Direction dir = parts.length > 1 && "desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return service.listAll(page, size, Sort.by(dir, parts[0]));
    }

    @GetMapping("/{id}")
    public Resource get(@PathVariable Long id) { 
    	   return service.get(id); 
    	}

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Resource> create(@Valid @RequestBody ResourceRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Resource update(@PathVariable Long id, @Valid @RequestBody ResourceRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}

