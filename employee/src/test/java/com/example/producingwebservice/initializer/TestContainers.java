package com.example.producingwebservice.initializer;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class TestContainers {

    public static final Network TEST_NETWORK = Network.newNetwork();
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest").withNetwork(TEST_NETWORK);

    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest")).withNetwork(TEST_NETWORK);
}
