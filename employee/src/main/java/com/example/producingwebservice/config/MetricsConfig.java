package com.example.producingwebservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {

	private final MeterRegistry meterRegistry;

	@PostConstruct
	public void registerCustomMetrics() {
		meterRegistry.counter("employee.login.total");
		meterRegistry.counter("employee.logout.total");
		meterRegistry.counter("employee.registration.total");
		meterRegistry.counter("employee.pdf.downloads");

		for (var cache : new String[]{"employee", "task", "user"}) {
			meterRegistry.counter("employee.cache.hits", "cache", cache);
			meterRegistry.counter("employee.cache.misses", "cache", cache);
		}

		meterRegistry.counter("employee.ratelimit.blocked", "endpoint", "login");
		meterRegistry.counter("employee.ratelimit.blocked", "endpoint", "registration");

		meterRegistry.counter("employee.kafka.messages", "status", "processed");
		meterRegistry.counter("employee.kafka.messages", "status", "duplicate");

		meterRegistry.counter("employee.scheduler.locks", "status", "acquired");
		meterRegistry.counter("employee.scheduler.locks", "status", "skipped");
	}

	@Configuration
	public static class CommonTagsConfig {
		@Bean
		public MeterRegistryCustomizer<MeterRegistry> commonTags() {
			return registry -> registry.config().commonTags(
					"application", "employee",
					"instance", UUID.randomUUID().toString().substring(0, 8)
			);
		}
	}
}
