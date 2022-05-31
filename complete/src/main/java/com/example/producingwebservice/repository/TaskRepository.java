package com.example.producingwebservice.repository;

import com.example.producingwebservice.domain.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Override
    List<Task> findAll(); //todo зачем указывать если он есть по умолчанию ? // done чтобы возвращал List, а не Iterable
}
