package com.example.producingwebservice.repository;


import com.example.producingwebservice.entity.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
	@Override
	List<Task> findAll();

	void deleteById(Long id);
}
