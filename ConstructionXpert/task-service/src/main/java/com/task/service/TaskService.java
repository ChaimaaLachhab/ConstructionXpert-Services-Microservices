package com.task.service;

import com.task.exception.TaskNotFoundException;
import com.task.model.Task;
import com.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final RestTemplate restTemplate;
    private  final TaskRepository taskRepository;
    private static final String PROJECT_SERVICE_URL = "http://project-service/api/projects";

    public Task createTask(Task task) {
        Boolean existProject = restTemplate.getForObject(PROJECT_SERVICE_URL+"/"+task.getProjectId()+ "/exist", Boolean.class);
        if (Boolean.TRUE.equals(existProject)){
            return taskRepository.save(task);
        }else {
            throw new RuntimeException("project not found");
        }
    }

    public List<Task> getTasksByProjectId(Long projectId) throws TaskNotFoundException {
        Boolean existProject = restTemplate.getForObject(PROJECT_SERVICE_URL+"/"+projectId+ "/exist", Boolean.class);
        if (Boolean.TRUE.equals(existProject)){
            return taskRepository.findByProjectId(projectId);
        }
        throw new TaskNotFoundException(projectId);
    }


    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask (Long id, Task resourceDetails) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setDescription(resourceDetails.getDescription());
        task.setStartDate(resourceDetails.getStartDate());
        task.setEndDate(resourceDetails.getEndDate());
        task.setStatus(resourceDetails.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
        ResponseEntity.ok().build();
    }

    public Boolean existTask(Long id) {
        return taskRepository.findById(id).isPresent();
    }
}
