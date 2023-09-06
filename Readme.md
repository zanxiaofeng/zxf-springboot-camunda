# Camunda
## Database
- ACT_RE_*, 代表存储库(Repository)，存储静态数据，如流程定义，流程资源等。
- ACT_RU_*,　代表运行时(Runtime),存储动态数据，如流程实例，用户任务，变量，作业等运行时数据。
- ACT_ID_*, 代表身份(Identity),存储身份信息，如用户，组等。
- ACT_HI_*, 代表历史(History)，存储历史数据，如过去的流程实例，变量，任务等。
- ACT_GE_*, 代表通用(General)数据。
- ACT_RE_PROCDEF, 所有已经部署的流程定义，包含诸如详细版本，资源名称，挂起状态等信息。
- ACT_RU_



## Document
- https://github.com/camunda/camunda-bpm-examples/
- https://docs.camunda.org/manual/latest/user-guide/spring-boot-integration/
- camunda-bpm-spring-boot-starter-7.19.0.jar!/META-INF/spring-configuration-metadata.json



# zxf-springboot-camunda-h2
## Camunda UI
- http://localhost:8080/

## Actuator UI
- http://localhost:8080/actuator

## H2 UI
- http://localhost:8080/h2

## Start process instance
- http://localhost:8080/process/loanRequest/definitions
- http://localhost:8080/process/loanRequest/start
- http://localhost:8080/process/loanRequest/instances
- http://localhost:8080/process/loanRequest/tasks
- http://localhost:8080/process/loanRequest/tasks/{taskId}/complete

# zxf-springboot-camunda-saga

## app-1

## app-2

