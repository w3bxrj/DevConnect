package com.devconnect.devconnect.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;



@RestController 
public class HealthController {

    // URL GET /health

    @GetMapping("/health")
    public String getHealth(){
        return "SpringBoot Application running HEALTHY.";
    }
    
    // GET /api/v1/status

    @GetMapping("/api/v1/status")
    public Map<String,Object> getStatus(){
        Map<String,Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("service", "DevConnect");
        map.put("version", "1.0.0");
        map.put("timestamp", LocalDateTime.now().toString());

        return map;
    }
    

    // JACKSON - Converts  JAVA Objects to HTTP requests compatible formats
    
}
