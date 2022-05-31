package com.example.producingwebservice.repository;


import com.example.producingwebservice.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Override
    List<Employee> findAll(); //todo зачем указывать если он есть по умолчанию ? // done чтобы возвращал List, а не Iterable

}

