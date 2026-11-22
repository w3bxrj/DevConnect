package com.devconnect.DevConnect.controller;

import com.devconnect.DevConnect.model.Developer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    // In Memory Storage
    // ConcurrentHashMap - Thread safe hashmap - safe for concurrent requests.
    private final Map<Long, Developer> developers = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping
    public List<Developer> getDevelopers() {
        return new ArrayList<>(developers.values());
    }

    // Path Variable
    @GetMapping("/{id}")
    public Developer getDeveloperByID(@PathVariable Long id) {
        return developers.get(id);
    }

    @GetMapping("/search")
    public List<Developer> getDevelopersBySkill(
            @RequestParam String skill,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int cpunt) {

        List<Developer> developersWithSkill = new ArrayList<>();

        for (Developer developer : developers.values()) {
            if (developer.getSkills().contains(skill)) {
                developersWithSkill.add(developer);
            }
        }

        return developersWithSkill;
    }

    @PostMapping
    public ResponseEntity<Developer> addDeveloper(@RequestBody Developer developer) {

        // Duplicate Check
        for (Developer existingDeveloper : developers.values()) {
            if (existingDeveloper.getEmail().equals(developer.getEmail()) ||
                    existingDeveloper.getPhoneNumber().equals(developer.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        developer.setId(idCounter.getAndIncrement());
        developers.put(developer.getId(), developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(developer);

    }

    // @PostMapping
    // public ResponseEntity<Developer> createDeveloper(@RequestBody Developer
    // developer) {
    // developer.setId(idCounter.getAndIncrement());
    // developers.put(developer.getId(), developer);
    // return ResponseEntity.status(HttpStatus.CREATED).build();
    // }

    @PutMapping("/{id}")
    public ResponseEntity<Developer> replaceDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        if (!developers.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        developers.remove(id);
        developer.setId(id);
        developers.put(id, developer);

        return ResponseEntity.status(HttpStatus.OK).body(developer);
    }

    
}
