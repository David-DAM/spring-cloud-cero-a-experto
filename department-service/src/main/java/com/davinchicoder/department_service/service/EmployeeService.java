package com.davinchicoder.department_service.service;

import com.davinchicoder.department_service.client.EmployeeClient;
import com.davinchicoder.department_service.model.Employee;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeClient employeeClient;

    @Retry(name = "department-service", fallbackMethod = "fallbackFindAllByDepartmentId")
    public List<Employee> findAllByDepartmentId(Long id) {
        return employeeClient.findAllByDepartmentId(id);
    }

    private List<Employee> fallbackFindAllByDepartmentId(Long id, Throwable ex) {
        log.error("Employee service is unavailable findAllByDepartmentId {}, with error {}", id, ex.getMessage());
        return new ArrayList<>();
    }

}
