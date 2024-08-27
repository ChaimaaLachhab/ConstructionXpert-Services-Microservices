package com.resource.controller;

import com.resource.exception.ResourceNotFoundException;
import com.resource.model.Resource;
import com.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping()
    public Resource createResource(Resource resource) {
        return resourceService.createResource(resource);
    }

    @GetMapping()
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping("/Task/{projectId}")
    public ResponseEntity<List<Resource>> getTasksByProjectId(@PathVariable Long projectId) throws ResourceNotFoundException {
        return ResponseEntity.ok(resourceService.getResourcesByTaskId(projectId));
    }

    @PutMapping("/resource/{id}")
    public Resource updateResource (@PathVariable Long id, @RequestBody Resource resourceDetails) throws ResourceNotFoundException {
        return resourceService.updateResource(id, resourceDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        ResponseEntity.ok().build();
    }
}
