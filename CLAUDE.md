# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

**This is a personal learning/demo project, NOT production code.** Hardcoded passwords in config files and docker-compose are intentional and should be ignored during code reviews.

## Build & Run

```bash
# Build all modules (JDK 21 required)
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn clean compile

# Package all modules
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn clean package -DskipTests

# Run individual saga apps (requires MySQL via docker-compose)
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-saga-app-1 spring-boot:run

# Run H2-based modules (no external DB needed)
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-h2 spring-boot:run
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-arch-app spring-boot:run
```

No Maven wrapper — use system `mvn`. No automated tests exist in the project.

## Tech Stack

- **Spring Boot 3.5.11** / **Camunda BPM 7.24** / **JDK 21**
- Jakarta EE namespace (`jakarta.annotation`, not `javax.annotation`)
- Lombok with `@Slf4j` for logging (never manual Logger fields)
- MySQL (saga modules, dual datasource) / H2 (h2 and arch-app modules)
- Flyway for database migrations (version managed by Spring Boot)

## Module Architecture

```
zxf-springboot-camunda (parent pom)
├── zxf-springboot-camunda-h2          (:8189) - Loan workflow reference with H2, FreeMarker templates
├── zxf-springboot-camunda-arch-app    (:8190) - Architecture reference: BPMN files, HTTP delegates, message correlation
├── zxf-springboot-camunda-arch-rest   (:8191) - Mock REST service (no Camunda, provides /task/a,b,c,d endpoints)
├── zxf-springboot-camunda-saga-common          - Shared library: SagaBuilder, dual datasource config, controllers, services
├── zxf-springboot-camunda-saga-app-1  (:8090) - Deploys App1Saga + App3Saga, deployment-aware=false
├── zxf-springboot-camunda-saga-app-2  (:8091) - Deploys App2Saga + App4Saga, deployment-aware=true
└── zxf-springboot-camunda-saga-app-3  (:8092) - Deploys all 4 sagas, deployment-aware=true
```

## Key Architectural Patterns

### Saga Pattern with Compensation
Saga processes are built **programmatically** via `SagaBuilder` (not BPMN files). Each `*Saga` class uses the fluent API to define activities, compensation activities, and retry behavior. BPMN XML is generated at startup and deployed to the Camunda engine.

- `App1Saga`/`App2Saga`: Compensation boundary with `triggerCompensationOnAnyError()`
- `App3Saga`/`App4Saga`: Retry-based with `R3/PT5S` retry cycles
- Retry disabled via `R0/PT0S` (FailedJobRetryTimeCycle)

### Dual Datasource (saga modules)
`DataSourceConfiguration` creates two independent datasources with separate transaction managers:
- **camundaBpmDataSource** — Camunda engine schema (MySQL :3306)
- **businessDataSource** — Business data schema (MySQL :3307)

Use `@Transactional(value="businessTransactionManager")` for business DB operations.

### Delegate/Adapter Pattern
All service tasks implement `JavaDelegate`. Task adapters are organized by saga (e.g., `App1Task1Adapter`, `App1Task1UndoAdapter` for compensation). Adapters in each saga-app module determine which jobs that app can process.

### Deployment Awareness
Controls which saga-app instance processes which jobs:
- `deployment-aware: false` — App processes all jobs (may fail if JavaDelegate class missing)
- `deployment-aware: true` — App only processes jobs from its registered deployments

### BPMN File Processes (arch-app, h2)
Static `.bpmn` files in `src/main/resources/bpmn/` — auto-deployed via `camunda.bpm.auto-deployment-enabled: true`.

## Configuration Properties (saga modules)

| Property | Purpose |
|----------|---------|
| `saga.re-deploy` | `true`=redeploy BPMN on startup (dev), `false`=reuse existing |
| `saga.register-deployment` | Register deployment with JobExecutor |
| `saga.async-before` / `saga.async-after` | Async job execution before/after activities |
| `saga.throw-exception` | Simulate failures for testing |
| `camunda.bpm.default-number-of-retries` | Must be high (11) for retry detection logic |
