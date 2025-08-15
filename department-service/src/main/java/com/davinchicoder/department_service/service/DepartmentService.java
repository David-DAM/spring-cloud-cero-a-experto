package com.davinchicoder.department_service.service;

import com.davinchicoder.department_service.model.Department;
import com.davinchicoder.department_service.repository.DepartmentRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;


    @CircuitBreaker(name = "department-service", fallbackMethod = "fallbackFindAll")
    @Bulkhead(name = "department-service", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "fallbackFindAllBulkHead")
    public List<Department> findAll() {

        List<Department> departments = departmentRepository.findAll();

        departments.forEach(department -> department.setEmployees(employeeService.findAllByDepartmentId(department.getId())));

        return departments;
    }

    @CircuitBreaker(name = "department-service", fallbackMethod = "fallbackFindById")
    public Optional<Department> findById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);

        if (optionalDepartment.isEmpty()) {
            return Optional.empty();
        }

        Department department = optionalDepartment.get();

        department.setEmployees(employeeService.findAllByDepartmentId(id));

        return Optional.of(department);
    }

    public void create(Department department) {
        departmentRepository.create(department);
    }

    private List<Department> fallbackFindAll(Throwable ex) {
        log.error("Department service is unavailable with error {}", ex.getMessage());
        return departmentRepository.findAll();
    }

    private Optional<Department> fallbackFindById(Long id, Throwable ex) {
        log.error("Department service is unavailable findById {}, with error {}", id, ex.getMessage());
        return departmentRepository.findById(id);
    }

    private List<Department> fallbackFindAllBulkHead(Throwable ex) {
        log.error("Department service is overloaded {}", ex.getMessage());
        return departmentRepository.findAll();
    }

}
