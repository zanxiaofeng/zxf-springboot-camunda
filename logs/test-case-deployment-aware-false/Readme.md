#
## async-before=true, async-after=true, include start-event
- [aTaskExecutor-1] : ==>  Preparing: select * from ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
- [aTaskExecutor-1] : ==> Parameters: app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819(String)
- [aTaskExecutor-1] : <==    Columns: ID_, REV_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, SUSPENSION_STATE_, JOB_PRIORITY_, TENANT_ID_, DEPLOYMENT_ID_
- [aTaskExecutor-1] : <==        Row: d9e0404c-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Start-app1-v10, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e0404d-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Start-app1-v10, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e0404e-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-1, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e0404f-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-1, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04050-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-2, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04051-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-2, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04052-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-3, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04053-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Task-3, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04054-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Undo-Task-1-compensation, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04055-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Undo-Task-1-compensation, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04056-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Undo-Task-2-compensation, async-continuation, async-before, 1, null, null, null
- [aTaskExecutor-1] : <==        Row: d9e04057-6f6f-11ef-9d5c-0681f720b819, 1, app1-v10:1:d9dff22b-6f6f-11ef-9d5c-0681f720b819, app1-v10, Activity-Undo-Task-2-compensation, async-continuation, async-after, 1, null, null, null
- [aTaskExecutor-1] : <==      Total: 12
## async-before=false, async-after=true, include start-event
- [nio-8090-exec-2] : ==>  Preparing: select * from ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
- [nio-8090-exec-2] : ==> Parameters: app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af(String)
- [nio-8090-exec-2] : <==    Columns: ID_, REV_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, SUSPENSION_STATE_, JOB_PRIORITY_, TENANT_ID_, DEPLOYMENT_ID_
- [nio-8090-exec-2] : <==        Row: dcd2523a-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Start-app1-v10, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==        Row: dcd2523b-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-1, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==        Row: dcd2523c-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-2, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==        Row: dcd2523d-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-3, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==        Row: dcd2523e-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Undo-Task-1-compensation, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==        Row: dcd2523f-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Undo-Task-2-compensation, async-continuation, async-after, 1, null, null, null
- [nio-8090-exec-2] : <==      Total: 6
## async-before=false, async-after=true, exclude start-event
2024-09-12T07:00:11.283+08:00 DEBUG 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : ==>  Preparing: select * from ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
2024-09-12T07:00:11.283+08:00 DEBUG 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : ==> Parameters: app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af(String)
2024-09-12T07:00:11.284+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==    Columns: ID_, REV_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, SUSPENSION_STATE_, JOB_PRIORITY_, TENANT_ID_, DEPLOYMENT_ID_
2024-09-12T07:00:11.284+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==        Row: 6dad5764-7091-11ef-b9fc-26d3701852af, 1, app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af, app1-v10, Activity-App1-Task-1, async-continuation, async-after, 1, null, null, null
2024-09-12T07:00:11.285+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==        Row: 6dad5765-7091-11ef-b9fc-26d3701852af, 1, app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af, app1-v10, Activity-App1-Task-2, async-continuation, async-after, 1, null, null, null
2024-09-12T07:00:11.285+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==        Row: 6dad5766-7091-11ef-b9fc-26d3701852af, 1, app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af, app1-v10, Activity-App1-Task-3, async-continuation, async-after, 1, null, null, null
2024-09-12T07:00:11.285+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==        Row: 6dad5767-7091-11ef-b9fc-26d3701852af, 1, app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af, app1-v10, Activity-App1-Undo-Task-1-compensation, async-continuation, async-after, 1, null, null, null
2024-09-12T07:00:11.285+08:00 TRACE 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==        Row: 6dad5768-7091-11ef-b9fc-26d3701852af, 1, app1-v10:3:6dad0943-7091-11ef-b9fc-26d3701852af, app1-v10, Activity-App1-Undo-Task-2-compensation, async-continuation, async-after, 1, null, null, null
2024-09-12T07:00:11.285+08:00 DEBUG 24904 --- [nio-8090-exec-1] electJobDefinitionsByProcessDefinitionId : <==      Total: 5

2024-09-12T07:10:39.396+08:00 DEBUG 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : ==>  Preparing: select * from ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
2024-09-12T07:10:39.396+08:00 DEBUG 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : ==> Parameters: app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af(String)
2024-09-12T07:10:39.396+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==    Columns: ID_, REV_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, SUSPENSION_STATE_, JOB_PRIORITY_, TENANT_ID_, DEPLOYMENT_ID_
2024-09-12T07:10:39.397+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523a-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Start-app1-v10, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.397+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523b-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-1, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.397+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523c-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-2, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.397+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523d-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Task-3, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.398+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523e-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Undo-Task-1-compensation, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.398+08:00 TRACE 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==        Row: dcd2523f-708f-11ef-9523-26d3701852af, 1, app1-v10:2:dcd20419-708f-11ef-9523-26d3701852af, app1-v10, Activity-App1-Undo-Task-2-compensation, async-continuation, async-after, 1, null, null, null
2024-09-12T07:10:39.398+08:00 DEBUG 31411 --- [aTaskExecutor-1] electJobDefinitionsByProcessDefinitionId : <==      Total: 6
