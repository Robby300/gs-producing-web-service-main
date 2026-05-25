package com.example.producingwebservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.UUID;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> commonTags() {
		return registry -> registry.config().commonTags(
				"application", "employee",
				"instance", UUID.randomUUID().toString().substring(0, 8)
		);
	}
}
