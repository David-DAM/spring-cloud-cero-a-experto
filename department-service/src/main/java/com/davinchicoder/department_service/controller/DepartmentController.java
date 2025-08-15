package com.davinchicoder.department_service.controller;

import com.davinchicoder.department_service.model.Department;
import com.davinchicoder.department_service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

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

}
