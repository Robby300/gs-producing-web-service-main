package com.example.producingwebservice.repository;

import io.spring.guides.gs_producing_web_service.Employee;
import io.spring.guides.gs_producing_web_service.Position;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmployeeRepository {
    private static final Map<String, Employee> employees = new HashMap<>();

    @PostConstruct
    public void initData() {
        Employee ivan = new Employee();
        ivan.setId(1);
        ivan.setName("Ivan");
        ivan.setPosition(Position.WORKER);
        ivan.setSalary(46704314);

        employees.put(ivan.getName(), ivan);

    }

    public Employee findEmployee(String name) {
        Assert.notNull(name, "The employee's name must not be null");
        return employees.get(name);
    }
}

