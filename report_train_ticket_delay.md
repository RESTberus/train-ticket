# Analysis of High Latency in Train Ticket Integration Testing

This report documents the root cause of the 5-second latency observed during the execution of automated testing tools (e.g., RestTestGen) against the Train Ticket microservices, specifically targeting the `ts-basic-service`.

## 1. Problem Description
During experiment `run12`, the request rate was significantly lower than expected. Tool logs revealed a consistent interval of approximately **5.02 seconds** between test interactions for the endpoint:
`GET /api/v1/basicservice/basic/{stationName}`

Despite the delay, the tool reported `HTTP 200 OK` for most of these requests, masking the underlying issue.

## 2. Root Cause Analysis

### 2.1 Hystrix Timeout Configuration
The primary cause of the 5-second delay is the **Hystrix Circuit Breaker** configuration in the `ts-basic-service`. In the controller class `fdse.microservice.controller.BasicController`, the default timeout is explicitly set to 5000ms:

```java
// train-ticket/ts-basic-service/src/main/java/fdse/microservice/controller/BasicController.java

@DefaultProperties(defaultFallback = "fallback", commandProperties = {
    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
})
public class BasicController {
    // ...
}
```

### 2.2 Execution Flow and Fallback
When a request is made to `GET /api/v1/basicservice/basic/{stationName}`, the execution flow is as follows:

1.  **Request Initiation**: The tool sends a request (often with a fuzzed/invalid `stationName`).
2.  **Downstream Call**: `ts-basic-service` attempts to call `ts-station-service` to retrieve the station ID.
3.  **Hanging Request**: If the `stationName` is invalid or if there are connectivity issues to `ts-station-service`, the `RestTemplate` call hangs.
4.  **Hystrix Timeout**: After exactly 5000ms, Hystrix triggers a timeout because the downstream call has not returned.
5.  **Fallback Execution**: Hystrix executes the defined `fallback()` method:
    ```java
    private HttpEntity fallback() {
        return ok(new Response<>());
    }
    ```
6.  **Response**: The fallback method returns an `HTTP 200 OK` with an empty `Response` object.

## 3. Impact on Testing
- **Low Throughput**: The 5-second "wait-then-fallback" cycle limits the testing tool to roughly 12 requests per minute per thread.
- **False Positives**: The tool perceives the requests as successful (200 OK), even though the actual business logic was never executed and a timeout occurred.
- **Inefficient Fuzzing**: Since RestTestGen intentionally sends invalid data to test robustness, it frequently triggers this timeout path, making the experiment progress extremely slowly.
