package com.davinchicoder.department_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments/assistant")
@RefreshScope
public class DepartmentAssistantController {

    @Value("${default.message:Hello}")
    private String defaultMessage;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(defaultMessage);
    }

}
