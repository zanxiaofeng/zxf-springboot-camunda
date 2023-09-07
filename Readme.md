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
- http://localhost:8090/saga/common?count=100
- http://localhost:8090/saga/app-1?count=100
- http://localhost:8090/info/definitions
- http://localhost:8090/info/instances
## app-2
- http://localhost:8091/saga/common?count=100
- http://localhost:8091/saga/app-2?count=100
- http://localhost:8091/info/definitions
- http://localhost:8091/info/instances

# 问题：
- 由异构应用（不同应用的不同版本）组成的Ｃanmunda集群中，如何实现特定应用版本与特定流程版本的绑定，以便特定版本的流程任务能被调度到支持的应用版本，以避免出现版本不匹配导致的错误（如找不到任务关联的JavaDelegate类）。

# 日志：
1. Controller一定要记录入口日志
2. 通过线程起的任务也要有入口日志
3. 其他如Event等的入口也要记录日志
4. 系统启动后全局只执行一次的PostConstruct类型任务要记录日志
5. Call downstream要记录完成日志（request, response）
6. 重要的代码分支一定要有日志并尽量附带上下文信息 
7. 日志一定要记录caseＩd以及相关上下文信息
8. 只使用slf４j记录日志
