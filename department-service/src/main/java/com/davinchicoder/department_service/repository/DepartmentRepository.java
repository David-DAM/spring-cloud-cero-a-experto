package com.davinchicoder.department_service.repository;

import com.davinchicoder.department_service.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepository {

    private final List<Department> departments;

    public DepartmentRepository() {
        departments = new ArrayList<>();
    }

    public List<Department> findAll() {
        return departments;
    }

    public void create(Department department) {
        departments.add(department);
    }

    public Optional<Department> findById(Long id) {
        return departments.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }
}
