# Roadmap

## Порядок внедрения

### 1. Micrometer + Prometheus + Grafana
Наблюдаемость — база для всего остального.

**Файлы:**
- `employee/build.gradle`
- `employee/src/main/resources/application.properties`
- `docker-compose.yml`
- `employee/src/main/java/.../config/MetricsConfig.java` — новый, конфиг MeterRegistry
- `employee/src/main/java/.../service/EmployeeServiceImpl.java`
- `employee/src/main/java/.../service/UserServiceImpl.java`
- `employee/src/main/java/.../service/TaskServiceImpl.java`
- `employee/src/main/java/.../controller/JwtAuthenticationController.java`
- `employee/src/main/java/.../config/security/RateLimitFilter.java`
- `employee/src/main/java/.../kafka/ConsumerListener.java`
- `employee/src/test/java/.../initializer/Initializer.java` — добавить Prometheus-контейнер
- `prometheus.yml` — новый, конфиг Prometheus в корне
- `grafana/datasources/datasource.yml` — новый
- `grafana/dashboards/jvm-dashboard.json` — новый (JVM Micrometer)
- `grafana/dashboards/employee-dashboard.json` — новый (кастомные метрики)

**Шаги:**

#### 1.1. Зависимости
```groovy
// build.gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
```

#### 1.2. Конфигурация Actuator
```properties
# application.properties
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.show-details=always
management.metrics.tags.application=${spring.application.name}
# Hibernate metrics
spring.jpa.properties.hibernate.generate_statistics=true
management.metrics.export.prometheus.enabled=true
```

#### 1.3. Кастомные метрики — MeterRegistry в каждой точке

| Метрика | Тип | Где | Теги |
|---------|-----|-----|------|
| `employee.login.total` | Counter | `JwtAuthenticationController.login()` | — |
| `employee.logout.total` | Counter | `JwtAuthenticationController.logout()` | — |
| `employee.registration.total` | Counter | `RegistrationController.addUser()` | — |
| `employee.created.total` | Counter | `ConsumerListener.executeTask()` | `source:kafka` |
| `employee.pdf.downloads` | Counter | `EmployeeServiceImpl.getEmployeePdfResponseEntity()` | — |
| `employee.cache.hits` | Counter | `EmployeeServiceImpl.findByUuid()`, `findAll()`, `TaskServiceImpl.*`, `UserServiceImpl.loadUserByUsername()` | `cache:employee`, `cache:task`, `cache:user` |
| `employee.cache.misses` | Counter | те же методы, ветка cache-miss | `cache:employee`, `cache:task`, `cache:user` |
| `employee.ratelimit.blocked` | Counter | `RateLimitFilter.doFilterInternal()` | `endpoint:login`, `endpoint:registration` |
| `employee.kafka.messages` | Counter | `ConsumerListener.executeTask()` | `status:processed`, `status:duplicate` |
| `employee.scheduler.locks` | Counter | `SchedulerConfig.deleteOneEmployee()` | `status:acquired`, `status:skipped` |

**Всего: ~10 counters + Timer для PDF-генерации + Gauge для размера кеша**

#### 1.4. MetricsConfig.java
```java
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
```

#### 1.5. Инфраструктура (docker-compose)

**prometheus.yml** (в корне проекта):
```yaml
global:
  scrape_interval: 15s
scrape_configs:
  - job_name: 'employee'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['employee:8081']
  - job_name: 'eureka'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['eureka:8761']
```

**docker-compose.yml** — добавить сервисы:
```yaml
prometheus:
  image: prom/prometheus:latest
  container_name: prometheus
  ports:
    - "9090:9090"
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  depends_on:
    - employee
    - eureka

grafana:
  image: grafana/grafana:latest
  container_name: grafana
  ports:
    - "3000:3000"
  environment:
    - GF_SECURITY_ADMIN_USER=admin
    - GF_SECURITY_ADMIN_PASSWORD=admin
  volumes:
    - ./grafana/datasources:/etc/grafana/provisioning/datasources
    - ./grafana/dashboards:/etc/grafana/provisioning/dashboards
  depends_on:
    - prometheus
```

#### 1.6. Grafana provisioning

**grafana/datasources/datasource.yml**:
```yaml
apiVersion: 1
datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
```

**grafana/dashboards/jvm-dashboard.json** — импорт дашборда `JVM Micrometer` (ID 4701 из grafana.com)
**grafana/dashboards/employee-dashboard.json** — кастомный дашборд с метриками employee

#### 1.7. Prometheus в TestContainers

Добавить в `TestContainers.java`:
```java
public static final GenericContainer<?> PROMETHEUS_CONTAINER =
    new GenericContainer<>(DockerImageName.parse("prom/prometheus:latest"))
        .withExposedPorts(9090)
        .withReuse(true);
```

#### 1.8. Что увидим в Grafana

1. **JVM Dashboard** — heap, GC, threads, CPU, file descriptors
2. **Employee Dashboard**:
   - Rate успешных/failed логинов (график)
   - Rate создания employee
   - Cache hit/miss ratio (важно: насколько эффективен Redis)
   - PDF download count
   - Rate limit blocked requests
   - Kafka message throughput (processed vs duplicate)
3. **Spring Boot Actuator** — health, info endpoints

### 2. Resilience4j
Защита существующих сервисов перед добавлением Gateway.

- [ ] Добавить `spring-cloud-starter-circuitbreaker-resilience4j`
- [ ] Circuit breaker на вызовы Kafka (producer, consumer)
- [ ] Retry на Eureka discovery
- [ ] Time limiter на PDF-генерацию (если PDF > 5s — fallback)
- [ ] Интегрировать с Micrometer (метрики circuit breaker)

### 3. Spring Cloud Gateway
Новый микросервис — единая точка входа.

- [ ] Добавить модуль `gateway` в проект
- [ ] Spring Cloud Gateway + Eureka discovery
- [ ] Route → employee-api (с префиксом /api/employee/**)
- [ ] Route → balance-api (с префиксом /api/balance/**)
- [ ] Rate limiting на уровне Gateway (вместо/вместе с самописным фильтром)
- [ ] Swagger aggregation всех сервисов
- [ ] Обновить docker-compose (gateway, порт 8080)

### 4. Debezium + Kafka Connect
Event-driven архитектура через CDC.

- [ ] Добавить Kafka Connect в docker-compose
- [ ] Настроить Debezium PostgreSQL connector (capture changes on employee, task, usr)
- [ ] Создать Kafka топики для событий (employee-created, employee-updated, task-assigned)
- [ ] Убрать явный producer из EmployeeServiceImpl — Debezium сам публикует изменения
- [ ] ConsumerListener переделать на обработку CDC-событий
- [ ] Интегрировать с existing Redis-кешем (инвалидация через CDC-события)
- [ ] Мониторинг через Micrometer + Prometheus

---

## Зависимости между шагами

```
Micrometer ──→ Resilience4j ──→ Gateway ──→ Debezium
     ↑                              ↑
     └──────── Metrics ─────────────┘
```

- **Шаг 1** независим — можно начать в любой момент
- **Шаг 2** зависит от шага 1 (Resilience4j отдаёт метрики в Micrometer)
- **Шаг 3** может идти параллельно с шагом 2 (Gateway — отдельный модуль)
- **Шаг 4** лучше после 1-3 — сложнее всех, нужна наблюдаемость и resilience
