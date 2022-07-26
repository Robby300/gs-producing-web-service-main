package com.example.producingwebservice;

import com.example.producingwebservice.initializer.TestContainers;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(
        scripts = {
                "/sql/delete_employee_tasks.sql",
                "/sql/delete_tasks.sql",
                "/sql/delete_employees.sql",

                "/sql/insert_employees.sql",
                "/sql/insert_tasks.sql",
                "/sql/insert_employee_tasks.sql",
        })
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainers.Initializer.class
})
@Transactional
public abstract class IntegrationTestBase {

    @BeforeAll
    static void init() {
        TestContainers.Initializer.start();
    }
}
