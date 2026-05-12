package user.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import user.dto.UserDto;
import user.service.UserService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/userservice/users")
@DefaultProperties(commandProperties = {
    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
})
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String testHello()
    {
        return "Hello";
    }

    @GetMapping
    public ResponseEntity getAllUser(@RequestHeader HttpHeaders headers)
    {
        return ok(userService.getAllUsers(headers));
    }

    @GetMapping("/{userName}")
    public ResponseEntity getUserByUserName(@PathVariable String userName, @RequestHeader HttpHeaders headers)
    {
        return ok(userService.findByUserName(userName, headers));
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity getUserByUserId(@PathVariable String userId, @RequestHeader HttpHeaders headers)
    {
        return ok(userService.findByUserId(userId, headers));
    }

    @PostMapping("/register")
    @HystrixCommand
    public ResponseEntity registerUser(@RequestBody UserDto userDto, @RequestHeader HttpHeaders headers)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto, headers));
    }

    @DeleteMapping("/{userId}")
    @HystrixCommand
    public ResponseEntity deleteUserById(@PathVariable String userId,
        @RequestHeader HttpHeaders headers)
    {
        // only admin token can delete
        return ok(userService.deleteUser(UUID.fromString(userId), headers));
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserDto user,
        @RequestHeader HttpHeaders headers)
    {
        return ok(userService.updateUser(user, headers));
    }
}
