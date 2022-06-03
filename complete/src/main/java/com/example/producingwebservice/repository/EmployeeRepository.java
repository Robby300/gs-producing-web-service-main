package com.example.producingwebservice.repository;


import com.example.producingwebservice.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Override
    List<Employee> findAll();

    Optional<Employee> findEmployeeByUuid(String uuid);

    void deleteEmployeeByUuid(String uuid);

}

