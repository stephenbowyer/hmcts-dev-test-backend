package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import uk.gov.hmcts.reform.dev.models.TaskModel;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping(value = "/api/task", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TaskModel> postTask(@RequestBody TaskModel task) {
        if (task.getTitle() == null || task.getTitle().isBlank()
            || task.getStatus() == null || task.getStatus().isBlank()
            || task.getDueDate() == null) {
            return ResponseEntity.badRequest().build();
        }
        TaskModel savedTask = taskRepository.save(task);
        System.out.println(savedTask);
        return ResponseEntity.status(201).body(savedTask);
    }

    @GetMapping(value = "/api/task/{id}", produces = "application/json")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Integer id) {
        TaskModel findTask = taskRepository.findById(id).orElse(null);
        if (findTask == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findTask);
    }

    @DeleteMapping(value = "/api/task/{id}")
    public ResponseEntity<TaskModel> deleteTask(@PathVariable Integer id) {
        TaskModel findTask = taskRepository.findById(id).orElse(null);
        if (findTask != null) {
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/api/task/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TaskModel> updateTaskStatus(@PathVariable Integer id, @RequestBody TaskModel task) {
        TaskModel findTask = taskRepository.findById(id).orElse(null);
        if (findTask != null) {
            findTask.setStatus(task.getStatus());
            taskRepository.save(findTask);
            return ResponseEntity.ok(findTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/api/task", produces = "application/json")
    public ResponseEntity<Iterable<TaskModel>> getAllTasks(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String status) {
        Iterable<TaskModel> allTasks = null;
        if (sort != null && status != null) {
            allTasks = taskRepository.findByStatus(status, Sort.by(sort));
        } else if (sort != null) {
            allTasks = taskRepository.findAll(Sort.by(sort));
        } else if (status != null) {
            allTasks = taskRepository.findByStatus(status);
        } else {
            allTasks = taskRepository.findAll();
        }
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping(value = "/api/task/status", produces = "application/json")
    public ResponseEntity<Iterable<Object[]>> getDistinctTaskStatuses() {
        Iterable<Object[]> distinctStatuses = taskRepository.findDistinctStatus();
        return ResponseEntity.ok(distinctStatuses);
    }

}
