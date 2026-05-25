package com.example.producingwebservice.repository;


import com.example.producingwebservice.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	@Override
	List<Employee> findAll();

	Optional<Employee> findEmployeeByUuid(String uuid);

	void deleteEmployeeByUuid(String uuid);
}
