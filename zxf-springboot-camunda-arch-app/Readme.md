# Camunda Web UI(camunda-bpm-spring-boot-starter-webapp)
- http://localhost:8080/camunda/app/

# Camunda Rest API(camunda-bpm-spring-boot-starter-api)
- http://localhost:8080/engine-rest

# H2 Web UI
- http://localhost:8080/h2

# Actuator Entrypoint
- http://localhost:8080/actuator
- http://localhost:8080/actuator/mappings

# Testing case success
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/payment/message-start?orderId=1
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived
- http://localhost:8190/payment/package-received?executionId=af66bbb5-b391-11ef-a8d4-0242e141deb4

# Testing case expired
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/payment/message-start?orderId=2
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived

# Testing case cancel
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/payment/message-start?orderId=3
- http://localhost:8190/info/executions/message?message=PaymentProcess.Cancel
- http://localhost:8190/payment/cancel?executionId=e7c47340-b776-11ef-95de-7606d5c7fdbe
