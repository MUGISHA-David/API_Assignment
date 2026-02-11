package auca.ac.rw.restfullApiAssignment.controller.task;

import auca.ac.rw.restfullApiAssignment.model.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(5);

    public TaskController() {
        tasks.add(new Task(1L, "Read book", "Read Clean Code", false, "MEDIUM", "2026-03-01"));
        tasks.add(new Task(2L, "Finish assignment", "Spring Boot REST API", false, "HIGH", "2026-02-20"));
        tasks.add(new Task(3L, "Workout", "Go to the gym", true, "LOW", "2026-02-15"));
        tasks.add(new Task(4L, "Buy groceries", "Weekly grocery shopping", false, "MEDIUM", "2026-02-13"));
        tasks.add(new Task(5L, "Call friend", "Catch up call", true, "LOW", "2026-02-12"));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam("completed") boolean completed) {
        List<Task> result = tasks.stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> result = tasks.stream()
                .filter(t -> t.getPriority() != null &&
                        t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        long newId = idGenerator.incrementAndGet();
        task.setTaskId(newId);
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updated) {
        for (Task t : tasks) {
            if (t.getTaskId().equals(taskId)) {
                t.setTitle(updated.getTitle());
                t.setDescription(updated.getDescription());
                t.setCompleted(updated.isCompleted());
                t.setPriority(updated.getPriority());
                t.setDueDate(updated.getDueDate());
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskCompleted(@PathVariable Long taskId) {
        for (Task t : tasks) {
            if (t.getTaskId().equals(taskId)) {
                t.setCompleted(true);
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(t -> t.getTaskId().equals(taskId));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

