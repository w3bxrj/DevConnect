package com.devconnect.devconnect.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devconnect.devconnect.model.Developer;

@RestController 
public class DeveloperController {
    
    // In-Memory Storage

    // ConcurrentHashMap - Thread safe hashmap - safe for concurrent requests

    private final Map<Long, Developer> developers = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/api/v1/developers")
    public List<Developer> getDeveloper(){
        return new ArrayList<>();
    }


    // Path Variable

    @GetMapping("/api/v1/developers/{id}")
    public Developer getDeveloperById(@PathVariable Long id){
        return developers.get(id);
    }

    @GetMapping("/api/v1/developers/search")
    public List<Developer> getDevelopersBySkill(
        @RequestParam(required = false) String skill,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(defaultValue = "0") int count
    ){
        List<Developer> developersWithSkill = new ArrayList<>();

        for(Developer developer : developers.values())
        {
            if(developer.getSkills().contains(skill))
                developersWithSkill.add(developer);
        }

        return developersWithSkill;
    }

    @PostMapping
    public Developer addDeveloper(@RequestBody Developer developer){
        developer.setId(idCounter.getAndIncrement());
        developers.put(developer.getId(), developer);
        return developer;
    }

    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer){
        developer.setId(idCounter.getAndIncrement());
        developers.put(developer.getId(), developer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    
    
}
