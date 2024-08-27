package com.resource.service;

import com.resource.exception.ResourceNotFoundException;
import com.resource.model.Resource;
import com.resource.repository.ResourceRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final RestTemplate restTemplate;
    @Autowired
    private ResourceRepository resourceRepository;
    private static final String TASK_SERVICE_URL = "http://task-service/api/tasks";

    public Resource createResource(Resource resource) {
        Boolean existTask = restTemplate.getForObject(TASK_SERVICE_URL+"/"+resource.getTaskId()+ "/exist", Boolean.class);
        if (Boolean.TRUE.equals(existTask)){
            return resourceRepository.save(resource);
        }else {
            throw new RuntimeException("Task not found");
        }
    }

    public List<Resource> getResourcesByTaskId(Long taskId) throws ResourceNotFoundException {
        Boolean existProject = restTemplate.getForObject(TASK_SERVICE_URL+"/"+taskId+ "/exist", Boolean.class);
        if (Boolean.TRUE.equals(existProject)){
            return resourceRepository.findByTaskId(taskId);
        }
        throw new ResourceNotFoundException(taskId);
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource updateResource (Long id, Resource resourceDetails) throws ResourceNotFoundException {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        resource.setName(resourceDetails.getName());
        resource.setQuantity(resourceDetails.getQuantity());
        resource.setType(resourceDetails.getType());
        resource.setProvider(resourceDetails.getProvider());
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
        ResponseEntity.ok().build();
    }
}
