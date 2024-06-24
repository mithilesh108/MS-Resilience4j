package com.cts.controller;

import com.cts.dto.CatalogDTO;
import com.cts.dto.UserDTO;
import com.cts.feign.CatalogFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class UserController {

    private List<UserDTO> list = new ArrayList<>();

    @Autowired
    private CatalogFeignClient catalogFeignClient;

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") int userId){
        String[] arr = {"Rohit","Shikar","Virat","Dhoni"};
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture<CatalogDTO>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int index = i;
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Running executorService thread");
                // Simulate a task that returns a result
                return findByName("mk");
            }, executorService);
        }
        // Step 4: Combine all CompletableFutures and wait for all of them to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all tasks to complete
        allOf.join();

        return ResponseEntity.status(HttpStatus.OK).body(list.stream()
                .filter(e -> e.getId() == userId).findFirst().orElseGet(() -> null));
    }

    int count =1;
    @GetMapping("/name/{name}")
    @Bulkhead(name = "catalogServiceName", fallbackMethod = "fallBackFindByName")
    //public CompletionStage<ResponseEntity> findByName(@PathVariable("name") String userName){
    public List<CatalogDTO> findByName(@PathVariable("name") String userName){
        System.out.println("User name : "+userName +" count-> "+count);
        catalogFeignClient.findByName(userName);
        System.out.println("User name : "+userName+" count-> "+count);
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<CompletableFuture<CatalogDTO>> futures = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            int index = i;
//            CompletableFuture<CatalogDTO> future = CompletableFuture.supplyAsync(() -> {
//                System.out.println("Running executorService thread");
//                // Simulate a task that returns a result
//                return catalogFeignClient.findByName(userName);
//            }, executorService);
//            futures.add(future);
//        }
        // Step 4: Combine all CompletableFutures and wait for all of them to complete
        //CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all tasks to complete
        //allOf.join();

        // Step 5: Collect the results
        List<CatalogDTO> results = new ArrayList<>();
//        for (CompletableFuture<CatalogDTO> future : futures) {
//            try {
//                results.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
         return results;
    }
    public List<CatalogDTO> fallBackFindByName(Exception ex) {
        System.out.println("fallBackFindByName method  "+ ex.getMessage());
        List<CatalogDTO> list = new ArrayList<>();
        list.add(CatalogDTO.builder().id(4).catalogType("electronics").price(15000).build());
        list.add(CatalogDTO.builder().id(5).catalogType("garments").price(5000).build());
        list.add(CatalogDTO.builder().id(6).catalogType("electronics").price(5000).build());
        list.add(CatalogDTO.builder().id(7).catalogType("garments").price(35000).build());
        return list;
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