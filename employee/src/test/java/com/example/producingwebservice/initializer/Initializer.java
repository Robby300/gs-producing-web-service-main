package com.example.producingwebservice.initializer;

import static com.example.producingwebservice.initializer.TestContainers.KAFKA_CONTAINER;
import static com.example.producingwebservice.initializer.TestContainers.POSTGRES_CONTAINER;
import static com.example.producingwebservice.initializer.TestContainers.REDIS_CONTAINER;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	public static void start() {
		POSTGRES_CONTAINER.start();
		KAFKA_CONTAINER.start();
		REDIS_CONTAINER.start();
	}

	@Override
	public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
		TestPropertyValues.of(
						"eureka.client.register-with-eureka=false",
						"spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
						"spring.flyway.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
						"spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
						"spring.flyway.user=" + POSTGRES_CONTAINER.getUsername(),
						"spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
						"spring.flyway.password=" + POSTGRES_CONTAINER.getPassword(),
						"spring.kafka.consumer.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers(),
						"spring.kafka.producer.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers(),
						"spring.data.redis.host=" + REDIS_CONTAINER.getHost(),
						"spring.data.redis.port=" + REDIS_CONTAINER.getMappedPort(6379))
				.applyTo(applicationContext);
	}
}
