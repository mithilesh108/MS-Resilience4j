package com.cts.controller;

import com.cts.dto.CatalogDTO;
import com.cts.dto.UserDTO;
import com.cts.feign.CatalogFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api")
public class UserController {

    private List<UserDTO> list = new ArrayList<>();

    @Autowired
    private CatalogFeignClient catalogFeignClient;

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") int userId){
        String[] arr = {"Rohit","Shikar","Virat","Dhoni"};
        for(int i=0;i<=arr.length;i++)
            System.out.println(arr[i]);
        System.out.println("user Id : "+userId);
        list.get(userId);
        System.out.println("user Id : "+userId);
        System.out.println(catalogFeignClient.findById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(list.stream()
                .filter(e -> e.getId() == userId).findFirst().orElseGet(() -> null));
    }

    @GetMapping("/name/{name}")
    @TimeLimiter(name = "catalogServiceName", fallbackMethod = "fallBackFindByName")
    //public CompletionStage<ResponseEntity> findByName(@PathVariable("name") String userName){
    public CompletableFuture findByName(@PathVariable("name") String userName){
        System.out.println("User name : "+userName);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Running a task asynchronously");
            catalogFeignClient.findByName(userName);
        });
        return future;
    }
    public CompletableFuture fallBackFindByName(Exception ex) {
        System.out.println("fallBackFindByName method  "+ ex.getMessage());
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Running a task fallBackFindByName");
        });
        return future;
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        System.out.println("Get all user");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        System.out.println("New UserDTO : "+userDTO);
        list.add(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostConstruct
    private void init(){
        list.add(UserDTO.builder().id(1).name("ram").address("delhi").salary(15000).build());
        list.add(UserDTO.builder().id(2).name("mk").address("delhi").salary(25000).build());
        list.add(UserDTO.builder().id(3).name("khan").address("chennai").salary(5000).build());
        list.add(UserDTO.builder().id(4).name("jorge").address("chennai").salary(15000).build());
        list.add(UserDTO.builder().id(5).name("shyam").address("delhi").salary(5000).build());
        list.add(UserDTO.builder().id(6).name("clob").address("hyd").salary(5000).build());
        list.add(UserDTO.builder().id(7).name("anish").address("hyd").salary(35000).build());
    }
}