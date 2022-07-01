package ru.liga.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableEurekaClient
public class BalanceGateway {
    public static void main(String[] args) {
        SpringApplication.run(BalanceGateway.class);
    }

    @Configuration
    public class SpringCloudConfig {

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route(r -> r.path("/**")
                            .uri("lb://EMPLOYEE")).build();
        }

    }
}
