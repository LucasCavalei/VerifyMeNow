package com.example.backend.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {
    @GetMapping("/home-data")
    public ResponseEntity<Map<String, Object>> getHomeData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "Bem-vindo à sua área restrita, " + currentUserName + "!");
        responseData.put("userId", currentUserName); // Ou obter o ID de outra forma a partir do 'authentication'
        responseData.put("someValue", 12345);
        responseData.put("status", "Authenticated");

        return ResponseEntity.ok(responseData);
    }
    // PROTECTED PROFESSOR ENDPOINT
    @GetMapping("/professor/dashboard-data")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Map<String, Object>> getProfessorData(Principal principal) {
        String username = principal.getName();
        Map<String, Object> professorData = Map.of(
                "userName", username,
                "course", "Advanced Topics in Spring Security",
                "studentCount", 25,
                "message", "Welcome Professor " + username + "!"
        );
        return ResponseEntity.ok(professorData);
    }

    @GetMapping("/student/grades")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> getStudentGrades(Principal principal) {
        return ResponseEntity.ok(Map.of("userName", principal.getName(), "grade", "A"));
    }
}


