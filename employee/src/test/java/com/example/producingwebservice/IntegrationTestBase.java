package com.example.producingwebservice;


import com.example.producingwebservice.initializer.Initializer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = {"/sql/insert_test_employees.sql", "/sql/insert_test_tasks.sql"})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {Initializer.class})
@EnableDiscoveryClient(autoRegister = false)
@Transactional
public abstract class IntegrationTestBase {

	@BeforeAll
	static void init() {
		Initializer.start();
	}
}
