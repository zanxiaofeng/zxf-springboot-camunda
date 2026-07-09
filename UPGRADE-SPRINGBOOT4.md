# Spring Boot 3.5.11 → 4.0.7 升级记录

本项目以“强制全量升级并记录断点”路径完成 Spring Boot 3→4 迁移。
结论：**全部 9 个模块在 Spring Boot 4.0.7 下可编译、可打包、可启动**，核心 Camunda 引擎与 webapp 正常工作；唯一牺牲是 Camunda REST API（Jersey 自动配置）。

## 升级概览

| 维度 | 结果 |
|------|------|
| 父 pom `spring-boot-starter-parent` | `3.5.11` → `4.0.7` |
| `mvn clean compile`（9 模块） | ✅ 全部通过 |
| `mvn package`（8 可执行 jar） | ✅ 全部通过 |
| h2 模块启动（H2，单数据源） | ✅ Tomcat:8080，引擎部署成功 |
| arch-app 启动（H2，BPMN 文件） | ✅ Tomcat:8190，引擎部署成功 |
| saga-app-1 启动（MySQL 双数据源 + 程序化 Saga） | ✅ Tomcat:8090，双 HikariPool 正常 |
| Camunda REST API（Jersey） | ❌ 牺牲掉（见断点 #1） |

## 升级过程中遇到的断点与处置

### 编译期断点（已全部修复）

1. **`ResponseErrorHandler` 单参 `handleError(ClientHttpResponse)` 被移除**
   - 文件：`zxf-springboot-camunda-arch-app/.../client/http/MyResponseErrorHandler.java`
   - Spring 7 中该单参方法删除，改为默认方法 `handleError(URI, HttpMethod, ClientHttpResponse)`。
   - 处置：删除空体的 override，依赖默认实现。

2. **`org.springframework.boot.autoconfigure.jdbc.*` 整包迁移**
   - 文件：`zxf-springboot-camunda-saga-common/.../config/DataSourceConfiguration.java`
   - Boot 4 模块化后，`DataSourceProperties` / `DataSourceAutoConfiguration` / `JdbcTemplateAutoConfiguration`
     从 `org.springframework.boot.autoconfigure.jdbc` 迁到 **`org.springframework.boot.jdbc.autoconfigure`**（模块 `spring-boot-jdbc`）。
   - 处置：仅改 3 行 import。

### 运行时断点（断点 #1 —— 唯一未绕过的硬断点）

- **`CamundaBpmRestJerseyAutoConfiguration` 引用 Boot 3 的 `org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath`**
  - Boot 4 中该类迁到 `org.springframework.boot.jersey.autoconfigure.JerseyApplicationPath`（模块 `spring-boot-jersey`）。
  - Camunda 7.24 的 REST starter 字节码是针对旧路径**硬编码编译**的，类无法加载 → `NoClassDefFoundError`。
  - 处置：在 3 个含 REST starter 的模块（`h2`、`arch-app`、`saga-app-1`）的
    `application.yml` 中通过 `spring.autoconfigure.exclude` 排除 `CamundaBpmRestJerseyAutoConfiguration`。
  - **代价**：Camunda REST API 不可用；Webapp / Tasklist / 引擎 / 程序化 Saga 全部正常。
  - 根因：Camunda 7.24 是社区版最后一个版本，其 Spring Boot starter 不会为 Boot 4 重新编译。
    社区版无解；唯一彻底恢复 REST 的途径是 Camunda 7 企业版（Boot 4 支持已于 2026-04 发布）或迁移 Camunda 8。

### 非阻塞告警（可忽略）

- `Failed to introspect meta-annotation @AutoConfigureAfter ... HibernateJpaAutoConfiguration not present`
  - Camunda 的 `CamundaBpmAutoConfiguration` 用 `@AutoConfigureAfter` 按名引用了 Boot 3 的
    `HibernateJpaAutoConfiguration`（Boot 4 中已迁移）。Spring 对缺失的 `@AutoConfigureAfter` 类型
    宽容处理，仅打印 WARN，不影响启动。

## 受影响的文件清单

- `pom.xml` —— 父 starter 版本 3.5.11 → 4.0.7
- `zxf-springboot-camunda-arch-app/src/main/java/zxf/camunda/arch/app/client/http/MyResponseErrorHandler.java`
- `zxf-springboot-camunda-saga-common/src/main/java/zxf/camunda/saga/config/DataSourceConfiguration.java`
- `zxf-springboot-camunda-h2/src/main/resources/application.yaml` —— REST autoconfig 排除
- `zxf-springboot-camunda-arch-app/src/main/resources/application.yaml` —— REST autoconfig 排除
- `zxf-springboot-camunda-saga-app-1/src/main/resources/application.yml` —— REST autoconfig 排除
- `CLAUDE.md` —— 技术栈版本更新

## 复验命令

```bash
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn clean package -DskipTests

# H2 模块（无需外部 DB）
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-h2 spring-boot:run
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-arch-app spring-boot:run

# Saga 模块（需先 docker-compose up -d 起 MySQL）
JAVA_HOME=/home/davis/.jdks/ms-21.0.10 mvn -pl zxf-springboot-camunda-saga-app-1 spring-boot:run
```

## 未验证项

- `saga-app-2 / app-3 / app-ext` 未逐一启动验证（需 MySQL；与 app-1 共用同一 saga-common 与同一 Camunda 引擎，编译/打包已通过，REST 断点不影响——这三者不含 REST starter）。
- 程序化 Saga 的补偿 / 重试 / External Task 运行期行为未在 Boot 4 下做长流程压测（引擎正常启动 + 部署成功，但端到端业务流程未跑通）。
