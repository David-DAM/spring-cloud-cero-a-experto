package com.davinchicoder.department_service.controller;

import com.davinchicoder.department_service.model.Department;
import com.davinchicoder.department_service.service.DepartmentService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @RateLimiter(name = "department-service", fallbackMethod = "fallbackFindAllRateLimiter")
    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Department> findById(@PathVariable Long id) {

        Optional<Department> optionalDepartment = departmentService.findById(id);

        return optionalDepartment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Department department) {
        departmentService.create(department);
        return ResponseEntity.ok().build();
    }

    @TimeLimiter(name = "department-service", fallbackMethod = "fallbackFindAllTimeLimiter")
    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<Department>>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(departmentService.findAll()));
    }

    private ResponseEntity<List<Department>> fallbackFindAllRateLimiter(Throwable ex) {
        log.error("Department service too many request {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    private CompletableFuture<ResponseEntity<List<Department>>> fallbackFindAllTimeLimiter(Throwable ex) {
        log.error("Department service time limite {}", ex.getMessage());
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT));
    }

}
