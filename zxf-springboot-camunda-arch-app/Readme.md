# Camunda Web UI(camunda-bpm-spring-boot-starter-webapp)
- http://localhost:8190/camunda/app/

# Camunda Rest API(camunda-bpm-spring-boot-starter-api)
- http://localhost:8190/engine-rest

# H2 Web UI
- http://localhost:8190/h2

# Actuator Entrypoint
- http://localhost:8190/actuator
- http://localhost:8190/actuator/mappings

# Payment flow
## Testing case success
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/payment/normal-start?orderId=1&paymentOrderCode=200&shippingRequestCode=200&shippingOrderCode=200
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived
- http://localhost:8190/app/payment/package-received?executionId=af66bbb5-b391-11ef-a8d4-0242e141deb4

## Testing case expired
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/payment/message-start?orderId=2&paymentOrderCode=200&shippingRequestCode=200&shippingOrderCode=200
- http://localhost:8190/info/executions/message?message=PaymentProcess.PackageReceived

## Testing case cancel
- http://localhost:8190/info/definitions/message?message=PaymentProcess.Start
- http://localhost:8190/app/payment/message-start?orderId=3&paymentOrderCode=200&shippingRequestCode=200&shippingOrderCode=200
- http://localhost:8190/info/executions/message?message=PaymentProcess.Cancel
- http://localhost:8190/app/payment/cancel?executionId=04789986-d3f7-11ef-b825-c6118aa45c47

# Form flow(sync create + sync message)
## Testing case success
- POST http://localhost:8190/app/form/start { "code": 200 }
- POST http://localhost:8190/app/form/message?formId=d0c36fbc-fcda-4806-b51d-08ab00ad07f0&action=update { "code": 200 }
- POST http://localhost:8190/app/form/message?formId=d0c36fbc-fcda-4806-b51d-08ab00ad07f0&action=delete { "code": 200 }


