# Camunda
## Database
- ACT_RE_*, 代表存储库(Repository)，存储静态数据，如流程定义，流程资源等。
- ACT_RU_*,　代表运行时(Runtime),存储动态数据，如流程实例，用户任务，变量，作业等运行时数据。
- ACT_ID_*, 代表身份(Identity),存储身份信息，如用户，组等。
- ACT_HI_*, 代表历史(History)，存储历史数据，如过去的流程实例，变量，任务等。
- ACT_GE_*, 代表通用(General)数据。
- ACT_RE_PROCDEF, 所有已经部署的流程定义，包含诸如详细版本，资源名称，挂起状态等信息。
- ACT_RE_DEPLOYMENT, 
- ACT_RU_JOBDEF, Job DEF of PROC DEF
- ACT_RU_EXECUTION
- ACT_RU_VARIABLE
- ACT_RU_JOB, For automation operation
- ACT_RU_TASK, For manual operation


## Document
- https://github.com/camunda/camunda-bpm-examples/
- https://docs.camunda.org/manual/latest/user-guide/spring-boot-integration/
- camunda-bpm-spring-boot-starter-7.21.0.jar!/META-INF/spring-configuration-metadata.json
- camunda-engine-7.21.0.jar!/org/camunda/bpm/engine/impl/mapping/entity/ProcessDefinition.xml


## Key Classes
### SpringBoot Camunda Starter
- org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultDatasourceConfiguration
### SpringBoot Transaction Manager
- org.springframework.transaction.interceptor.TransactionInterceptor.invoke
- org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction
### SpringBoot Auto Configuration - CONDITIONS EVALUATION REPORT(For SpringBoot 2,3)
- org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener
### SpringBoot Auto Configuration - AUTO-CONFIGURATION REPORT(For SpringBoot 1)
- org.springframework.boot.autoconfigure.logging.AutoConfigurationReportLoggingInitializer
### SpringBoot flyway config
- org.springframework.boot.autoconfigure.flyway.FlywayProperties
- org.springframework.boot.sql.init.dependency.DatabaseInitializerDetector=org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializerDatabaseInitializerDetector
### Spring Task Executor
- org.springframework.core.task.TaskExecutor
- org.springframework.core.task.AsyncTaskExecutor
- org.springframework.scheduling.SchedulingTaskExecutor
- org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
### Key classes of Camunda
- org.camunda.bpm.engine.impl.ProcessEngineLogger
- org.camunda.bpm.engine.impl.jobexecutor.JobExecutor.startJobAcquisitionThread
- org.camunda.bpm.engine.impl.jobexecutor.ThreadPoolJobExecutor
- org.camunda.bpm.engine.impl.jobexecutor.DefaultJobExecutor
- org.camunda.bpm.engine.impl.persistence.entity.JobManager.findNextJobsToExecute

# Core Concept
- The process engine is a piece of passive Java code which works in the Thread of the client. For instance, if you have a web application allowing users to start a new process instance and a user clicks on the corresponding button, some thread from the application server’s http-thread-pool will invoke the API method runtimeService.startProcessInstanceByKey(...), thus entering the process engine and starting a new process instance. We call this “borrowing the client thread”.
- On any such external trigger (i.e., start a process, complete a task, signal an execution), the engine runtime will advance in the process until it reaches wait states on each active path of execution. A wait state is a task which is performed later, which means that the engine persists the current execution to the database and waits to be triggered again. For example in case of a user task, the external trigger on task completion causes the runtime to execute the next bit of the process until wait states are reached again (or the instance ends). In contrast to user tasks, a timer event is not triggered externally. Instead it is continued by an internal trigger. That is why the engine also needs an active component, the job executor, which is able to fetch registered jobs and process them asynchronously.

# Retry and Compensate Event
- By default, a failed job will be retried three times and the retries are performed immediately after the failure
- Compensate Event is high priority than Retry event.
- We can disable the retry by set FailedJobRetryTimeCycle=R0/PT0S
- 在Retry模式下，如果发生异常，当前Job的状态变更会被回顾，新设置的变量会丢失。
- 在Compensate模式下，如果发生异常，当前Job的状态会被持久化，新设置的变量不会失去。

# Deployment and Version
- Deployment即是一次部署，指特定的版本
- Deployment Aware即是指Job Executor和Deployment(版本)绑定，Job Executor只能处理特定Deployment(版本)的任务.
- Deployment Aware=false模式下的进程可以获取到当前系统中的所有的流程任务，如果流程执行需要的Java Delegate类找不到, 则会报错.
- Deployment Aware=true模式下的进程只能获取到特定Deployment相关的任务，Job acquisition在获取任务时将根据RegisterDeploymentForJobExecutor来过滤任务.
- Deployment Aware的作用是确保作业执行器只执行与特定部署相关联的作业，这有助于在多环境部署和流程版本管理中保持作业的一致性和正确性。

# Register Deployment For JobExecutor
- RegisterDeploymentForJobExecutor只有在Deployment Aware=true时才有效.
- 进程部署或重新部署的流程Deployment会在部署后自动注册.
- 可以通过调用ManagementService::registerDeploymentForJobExecutor来注册特定的部署.

# zxf-springboot-camunda-saga
## Camunda Web UI(Setup admin)
- http://localhost:8090/
- http://localhost:8091/
- http://localhost:8092/
## Camunda Rest Api
- http://localhost:8090/engine-rest/*
- http://localhost:8091/engine-rest/*
- http://localhost:8092/engine-rest/*
## app-1(变更流程需要升级版本)
- http://localhost:8090/saga/all?count=100
- http://localhost:8090/saga/app-1?count=100
- http://localhost:8090/saga/app-2?count=100
- http://localhost:8090/saga/app-3?count=100
- http://localhost:8090/saga/byId?count=100&processDefinitionId=zxf-common-v3.8:11:734c0b76-6be9-11ef-aa6f-ae65bba2d6aa
- http://localhost:8090/info/definitions
- http://localhost:8090/info/instances
- http://localhost:8091/info/jobs/all
- http://localhost:8091/info/jobs/failed
- http://localhost:8090/info/deployments/registered
## app-2(变更流程需要升级版本)
- http://localhost:8091/saga/all?count=100
- http://localhost:8091/saga/app-1?count=100
- http://localhost:8091/saga/app-2?count=100
- http://localhost:8091/saga/app-3?count=100
- http://localhost:8091/saga/byId?count=100&processDefinitionId=zxf-common-v3.8:11:734c0b76-6be9-11ef-aa6f-ae65bba2d6aa
- http://localhost:8091/info/definitions
- http://localhost:8091/info/instances
- http://localhost:8091/info/jobs/all
- http://localhost:8091/info/jobs/failed
- http://localhost:8091/info/deployments/registered

## app-3(变更流程需要升级版本)
- http://localhost:8092/saga/all?count=100
- http://localhost:8092/saga/app-1?count=100
- http://localhost:8092/saga/app-2?count=100
- http://localhost:8092/saga/app-3?count=100
- http://localhost:8092/saga/byId?count=100&processDefinitionId=zxf-common-v3.8:11:734c0b76-6be9-11ef-aa6f-ae65bba2d6aa
- http://localhost:8092/info/definitions
- http://localhost:8092/info/instances
- http://localhost:8092/info/jobs/all
- http://localhost:8092/info/jobs/failed
- http://localhost:8092/info/deployments/registered

# re-deploy & deployment-aware 模式
- re-deploy=false，只在开发模式下使用
- re-deploy=true, deployment-aware=true，必须用于生产模式
- deployment-aware=true模式下，App1只能收到app1Saga和app3Saga的特定版本的任务,App2只能收到app2Saga和app4Saga的特定版本的任务,App3能收到app1Saga,app2Saga,app3Saga,app4Saga的特定版本的任务.
- deployment-aware=false模式下，App1，App2，App3都能收到app1Saga,app2Saga,app3Saga,app4Saga的任务(不区分版本)，App3能成功执行所有Saga的任务，App1只能成功执行app1Saga,app3Sage的任务，App2只能成功执行app2Saga,app4Sage的任务; App1,App2不能处理部分Saga任务是因为相关Task的Java类不存在，执行会报错ProcessEngineException(cause=ClassNotFoundException).

# Job & JavaDelegate
- App从Camunda数据库中获取到的Job，如果该Job相关联的JavaDelegate类不存在，执行就会出错.ProcessEngineException(cause=ClassNotFoundException)
- deployment-aware=false模式下,App可以从Camunda数据库中获取到任何的Job.
- deployment-aware=true模式下,App只可以从Camunda数据库中获取到与已注册Deployment相关的Job.

# Job Acquisition
- 连续多次获取不到Job，Job Acquisition会逐渐降低获取Job的频率，直到下次获取到Job后Reset.
- 当前实例在调用startProcessInstanceBy**函数后，会立即触发Job Acquisition并Reset Job Acquisition.

# 问题：
## 问题一
- 由异构应用（不同应用的不同版本）组成的Camunda集群中，如何实现特定应用版本与特定流程版本的绑定，以便特定版本的流程任务能被调度到支持的应用版本，以避免出现版本不匹配导致的错误（如找不到任务关联的JavaDelegate类）。
## 问题二
- 如何确定那个进程能处理那些Job呢？是看Job对应的Class是否在进程中存在吗？
- 是否一个工作流实例中的多个Java Activity Job可以被分散调度到多个不同的Java进程里执行? 
- 如果一个Java进程里没有包含运行一个工作流实例的所有Java Activity Job对应的Java类，会发生什么？

# 日志：
1. Controller一定要记录入口日志
2. 通过线程起的任务也要有入口日志
3. 其他如Event等的入口也要记录日志
4. 系统启动后全局只执行一次的PostConstruct类型任务要记录日志
5. Call downstream要记录完成日志（request, response）
6. 重要的代码分支一定要有日志并尽量附带上下文信息 
7. 日志一定要记录caseＩd以及相关上下文信息
8. 只使用slf４j记录日志
