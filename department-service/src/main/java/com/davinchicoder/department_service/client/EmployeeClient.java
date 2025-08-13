package com.davinchicoder.department_service.client;

import com.davinchicoder.department_service.model.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/api/v1/employees/department/{id}")
    List<Employee> findAllByDepartmentId(@PathVariable Long id);

}
