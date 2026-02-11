package auca.ac.rw.restfullApiAssignment.controller.userprofile;

import auca.ac.rw.restfullApiAssignment.model.userprofile.ApiResponse;
import auca.ac.rw.restfullApiAssignment.model.userprofile.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final List<UserProfile> profiles = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(3);

    public UserProfileController() {
        profiles.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe",
                28, "Rwanda", "Software engineer", true));
        profiles.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith",
                25, "Kenya", "UI/UX designer", true));
        profiles.add(new UserProfile(3L, "mark_brown", "mark@example.com", "Mark Brown",
                32, "Uganda", "Project manager", false));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllProfiles() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "All user profiles", profiles)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getProfileById(@PathVariable Long userId) {
        Optional<UserProfile> profile = profiles.stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst();

        if (profile.isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User profile found", profile.get())
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User profile not found", null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createProfile(@RequestBody UserProfile profile) {
        long newId = idGenerator.incrementAndGet();
        profile.setUserId(newId);
        profiles.add(profile);
        ApiResponse<UserProfile> response =
                new ApiResponse<>(true, "User profile created successfully", profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserProfile updated) {
        for (UserProfile p : profiles) {
            if (p.getUserId().equals(userId)) {
                p.setUsername(updated.getUsername());
                p.setEmail(updated.getEmail());
                p.setFullName(updated.getFullName());
                p.setAge(updated.getAge());
                p.setCountry(updated.getCountry());
                p.setBio(updated.getBio());
                p.setActive(updated.isActive());
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "User profile updated successfully", p)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User profile not found", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long userId) {
        boolean removed = profiles.removeIf(p -> p.getUserId().equals(userId));
        if (removed) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "User profile deleted successfully", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User profile not found", null));
    }

    @GetMapping("/search/username")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByUsername(
            @RequestParam("username") String username) {
        List<UserProfile> result = profiles.stream()
                .filter(p -> p.getUsername() != null &&
                        p.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Search by username result", result)
        );
    }

    @GetMapping("/search/country")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByCountry(
            @RequestParam("country") String country) {
        List<UserProfile> result = profiles.stream()
                .filter(p -> p.getCountry() != null &&
                        p.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Search by country result", result)
        );
    }

    @GetMapping("/search/age-range")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByAgeRange(
            @RequestParam("min") int min,
            @RequestParam("max") int max) {
        List<UserProfile> result = profiles.stream()
            .filter(p -> p.getAge() >= min && p.getAge() <= max)
            .collect(Collectors.toList());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Search by age range result", result)
        );
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateProfile(@PathVariable Long userId) {
        for (UserProfile p : profiles) {
            if (p.getUserId().equals(userId)) {
                p.setActive(true);
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "User profile activated", p)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User profile not found", null));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateProfile(@PathVariable Long userId) {
        for (UserProfile p : profiles) {
            if (p.getUserId().equals(userId)) {
                p.setActive(false);
                return ResponseEntity.ok(
                        new ApiResponse<>(true, "User profile deactivated", p)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User profile not found", null));
    }
}

