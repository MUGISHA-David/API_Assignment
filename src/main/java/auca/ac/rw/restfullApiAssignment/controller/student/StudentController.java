package auca.ac.rw.restfullApiAssignment.controller.student;

import auca.ac.rw.restfullApiAssignment.model.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(5);

    public StudentController() {
        students.add(new Student(1L, "Alice", "Smith", "alice@example.com", "Computer Science", 3.8));
        students.add(new Student(2L, "Bob", "Johnson", "bob@example.com", "Information Technology", 3.2));
        students.add(new Student(3L, "Carol", "Davis", "carol@example.com", "Computer Science", 3.6));
        students.add(new Student(4L, "David", "Brown", "david@example.com", "Business", 3.9));
        students.add(new Student(5L, "Eve", "Wilson", "eve@example.com", "Mathematics", 2.9));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Optional<Student> student = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> result = students.stream()
                .filter(s -> s.getMajor() != null &&
                        s.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam("gpa") Double minGpa) {
        List<Student> result = students.stream()
                .filter(s -> s.getGpa() != null && s.getGpa() >= minGpa)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        long newId = idGenerator.incrementAndGet();
        student.setStudentId(newId);
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student updated) {
        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                s.setFirstName(updated.getFirstName());
                s.setLastName(updated.getLastName());
                s.setEmail(updated.getEmail());
                s.setMajor(updated.getMajor());
                s.setGpa(updated.getGpa());
                return ResponseEntity.ok(s);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

