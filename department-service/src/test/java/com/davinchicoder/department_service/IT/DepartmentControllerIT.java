package com.davinchicoder.department_service.IT;


import com.davinchicoder.department_service.model.Department;
import com.davinchicoder.department_service.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureStubRunner(
        ids = "com.davinchicoder:department-service:+:stubs:8082",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class DepartmentControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    public void findById() {

        Department build = Department.builder().id(1L).name("Compras").build();

        departmentRepository.create(build);

        Department department = restTemplate.getForObject("/api/v1/departments/1", Department.class);

        assertNotNull(department);
        assertEquals(build.getName(), department.getName());
        assertEquals(1, department.getEmployees().size());
    }
}
