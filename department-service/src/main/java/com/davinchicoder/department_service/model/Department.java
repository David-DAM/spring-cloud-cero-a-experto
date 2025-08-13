package com.davinchicoder.department_service.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Department {

    Long id;
    String name;
    List<Employee> employees;

}
