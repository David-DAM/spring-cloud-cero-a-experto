package contracts.employee_service

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/api/v1/employees/department/1'
    }
    response {
        status 200
        body([
                [
                        id          : 1L,
                        departmentId: 1L,
                        name        : "John Doe",
                        position    : "Engineer"
                ]

        ])
        headers {
            contentType(applicationJson())
        }
    }
}
