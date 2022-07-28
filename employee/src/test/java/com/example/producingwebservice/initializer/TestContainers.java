package com.example.producingwebservice.initializer;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class TestContainers {
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest").withReuse(true);

    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
                    .withNetwork(null)
                    .withReuse(true);
}
