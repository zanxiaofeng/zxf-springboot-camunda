# Test Step
## Step 1
- Disable job executor of App1
- Start App1
- curl http://localhost:8090/saga/app-1?count=1
- curl http://localhost:8090/saga/app-2?count=1
- curl http://localhost:8090/saga/app-3?count=1
## Step 2
- Enable job executor of App2
- Start App2
- There will be exceptions of ProcessEngineException(cause=ClassNotFoundException) in logs.