# Roadmap

## Порядок внедрения

### 1. Micrometer + Prometheus + Grafana
Наблюдаемость — база для всего остального.

- [ ] Добавить `micrometer-registry-prometheus` в build.gradle
- [ ] Настроить Actuator (включить prometheus endpoint)
- [ ] Добавить Prometheus в docker-compose
- [ ] Добавить Grafana в docker-compose (дашборды: JVM, Kafka, HikariCP)
- [ ] Добавить кастомные метрики (количество логинов, созданных employee, PDF-скачиваний)
- [ ] Настроить алерты (e.g., high error rate)

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
