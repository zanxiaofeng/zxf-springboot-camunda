# Test Step
## Step 1
- Initial start app1 for create tables
- Start App1(job-execution.enable=false, job-execution.deployment-aware=false)
- curl http://localhost:8090/saga/app-1?count=1
- curl http://localhost:8090/saga/app-2?count=1
- curl http://localhost:8090/saga/app-3?count=1
## Step 2
- Start App2(job-execution.enable=true, job-execution.deployment-aware=false)
- There will be exceptions of ProcessEngineException(cause=ClassNotFoundException) in logs.