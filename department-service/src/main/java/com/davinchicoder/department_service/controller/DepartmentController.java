package com.davinchicoder.department_service.controller;

import com.davinchicoder.department_service.client.EmployeeClient;
import com.davinchicoder.department_service.model.Department;
import com.davinchicoder.department_service.repository.DepartmentRepository;
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

    private final DepartmentRepository departmentRepository;
    private final EmployeeClient employeeClient;

    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        List<Department> departments = departmentRepository.findAll();
        departments.forEach(department -> department.setEmployees(employeeClient.findAllByDepartmentId(department.getId())));
        return ResponseEntity.ok(departments);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Department> findById(@PathVariable Long id) {

        Optional<Department> optionalDepartment = departmentRepository.findById(id);

        if (optionalDepartment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Department department = optionalDepartment.get();

        department.setEmployees(employeeClient.findAllByDepartmentId(id));

        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Department department) {
        departmentRepository.create(department);
        return ResponseEntity.ok().build();
    }

}
