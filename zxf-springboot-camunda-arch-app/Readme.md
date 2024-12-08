# Camunda UI
- http://localhost:8190/

# Actuator UI
- http://localhost:8190/actuator

# H2 UI
- http://localhost:8190/h2

# Testing case success
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/message-start?orderId=1
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived
- http://localhost:8190/app/package/received?executionId=af66bbb5-b391-11ef-a8d4-0242e141deb4

# Testing case expired
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/message-start?orderId=2
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived

# Testing case cancel
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/message-start?orderId=3
- http://localhost:8190/info/executions/message?message=PaymentProcess.Cancel
- http://localhost:8190/app/payment/cancel?executionId=af66bbb5-b391-11ef-a8d4-0242e141deb4
