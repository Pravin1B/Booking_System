package com.booking.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.booking.dto.ResourceRequest;
import com.booking.entity.Resource;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.ResourceRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ResourceService {
    private final ResourceRepository repo;
    public ResourceService(ResourceRepository repo) {
    	  this.repo = repo;  
    	}

    public Page<Resource> listAll(int page, int size, Sort sort) {
        return repo.findAll(PageRequest.of(page, size, sort));
    }

    public Resource get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public Resource create(ResourceRequest req) {
        Resource r = new Resource();
        r.setName(req.getName());
        r.setType(req.getType());
        r.setDescription(req.getDescription());
        r.setCapacity(req.getCapacity());
        r.setActive(req.getActive() == null ? true : req.getActive());
        return repo.save(r);
    }

    public Resource update(Long id, ResourceRequest req) {
        Resource r = get(id);
        if (req.getName() != null) r.setName(req.getName());
        if (req.getType() != null) r.setType(req.getType());
        if (req.getDescription() != null) r.setDescription(req.getDescription());
        if (req.getCapacity() != null) r.setCapacity(req.getCapacity());
        if (req.getActive() != null) r.setActive(req.getActive());
        return repo.save(r);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Resource not found");
        repo.deleteById(id);
    }
}

