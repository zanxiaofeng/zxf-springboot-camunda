# Camunda UI
- http://localhost:8080/

# Actuator UI
- http://localhost:8080/actuator

# H2 UI
- http://localhost:8080/h2

# Testing case 1
- http://localhost:8080/loan/request?amount=123
- http://localhost:8080/info/tasks/all
- http://localhost:8080/loan/approve?taskId=90cbb27b-b470-11ef-a500-02425d783d69&amount=100

# Testing case 2
- http://localhost:8080/loan/request?amount=300000
- http://localhost:8080/loan/updated?executionId=d1663db1-b470-11ef-a500-02425d783d69&amount=150000
- http://localhost:8080/info/tasks/all
- http://localhost:8080/loan/approve?taskId=eac06baf-b3c6-11ef-be29-0242e141deb4&amount=100000