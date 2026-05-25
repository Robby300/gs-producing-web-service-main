package com.example.producingwebservice.initializer;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public final class TestContainers {
	public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
			new PostgreSQLContainer<>("postgres:latest").withReuse(true);

	public static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
					DockerImageName.parse("confluentinc/cp-kafka:7.7.2"))
			.withNetwork(null)
			.withReuse(true);

	private TestContainers() {}
}
