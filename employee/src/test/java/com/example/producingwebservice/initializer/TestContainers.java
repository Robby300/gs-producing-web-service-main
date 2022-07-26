package com.example.producingwebservice.initializer;

import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class TestContainers {

    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:14.4");

    public static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                    "spring.flyway.url=" + POSTGRES_CONTAINER.getJdbcUrl(),

                    "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                    "spring.flyway.user=" + POSTGRES_CONTAINER.getUsername(),

                    "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                    "spring.flyway.password=" + POSTGRES_CONTAINER.getPassword(),

                    "spring.kafka.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers()
            ).applyTo(applicationContext);
        }

        public static void start() {
            POSTGRES_CONTAINER.start();
            KAFKA_CONTAINER.start();
        }
    }
}
