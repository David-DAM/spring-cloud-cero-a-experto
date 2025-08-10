package com.davinchicoder.employee_service.repository;

import com.davinchicoder.employee_service.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final List<Employee> employees;

    public EmployeeRepository() {
        employees = new ArrayList<>();
    }

    public List<Employee> findAll() {
        return employees;
    }

    public List<Employee> findByDepartmentId(Long departmentId) {
        return employees.stream()
                .filter(e -> e.getDepartmentId().equals(departmentId))
                .toList();
    }

    public void create(Employee employee) {
        employees.add(employee);
    }

}
