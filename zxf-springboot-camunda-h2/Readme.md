# Camunda Web UI(camunda-bpm-spring-boot-starter-webapp)
- http://localhost:8080/camunda/app/

# Camunda Rest API(camunda-bpm-spring-boot-starter-api)
- http://localhost:8080/engine-rest

# H2 Web UI
- http://localhost:8080/h2

# Actuator Entrypoint
- http://localhost:8080/actuator


# Testing case 1
- http://localhost:8080/info/definitions/message?message=LaonProcess.Start
- http://localhost:8080/loan/request?amount=123
- http://localhost:8080/info/tasks/all
- http://localhost:8080/loan/approve?taskId=90cbb27b-b470-11ef-a500-02425d783d69&amount=100

# Testing case 2
- http://localhost:8080/info/definitions/message?message=LaonProcess.Start
- http://localhost:8080/loan/request?amount=300000
- http://localhost:8080/info/executions/message?message=LoanProcess.LoanRequestUpdated
- http://localhost:8080/loan/updated?executionId=d1663db1-b470-11ef-a500-02425d783d69&amount=150000
- http://localhost:8080/info/tasks/all
- http://localhost:8080/loan/approve?taskId=73eabcd7-b4f7-11ef-a7d5-02425d783d69&amount=100000