package rebook;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import java.net.SocketTimeoutException;

@ControllerAdvice
public class GlobalTimeoutExceptionHandler {

    @ExceptionHandler({HystrixTimeoutException.class, ResourceAccessException.class, SocketTimeoutException.class})
    public ResponseEntity<String> handleTimeoutException(Exception ex) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Server Timeout: " + ex.getMessage());
    }
}
