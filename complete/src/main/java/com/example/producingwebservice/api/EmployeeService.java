package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    void deleteByUuid(String uuid);

    @Validated
    EmployeeResponse save(@Valid Employee employee);

    Employee getByUuid(String uuid); //todo название findByUuid. Потому что идет именно поиск
}
