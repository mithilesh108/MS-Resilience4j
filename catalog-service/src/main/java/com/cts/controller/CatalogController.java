package com.cts.controller;

import com.cts.dto.CatalogDTO;
import com.cts.dto.UserDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    private List<CatalogDTO> list = new ArrayList<>();

    @GetMapping("/id/{id}")
    public ResponseEntity<CatalogDTO> findById(@PathVariable("id") int userId){
        System.out.println("catalogType Id : "+userId);
        return ResponseEntity.status(HttpStatus.OK).body(list.stream()
                .filter(e -> e.getId() == userId).findFirst().orElseGet(() -> null));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity findByName(@PathVariable("name") String catalogType) throws InterruptedException {
        System.out.println("catalogType name start : "+catalogType);
        Thread.sleep(100000);
        System.out.println("catalogType name end : "+catalogType);
        return ResponseEntity.status(HttpStatus.OK).body(list.stream()
                .filter(e -> e.getCatalogType().equals(catalogType)).findAny().orElse(null));
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        System.out.println("Get all catalogDTO");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping
    public ResponseEntity<CatalogDTO> createUser(@RequestBody CatalogDTO catalogDTO){
        System.out.println("New UserDTO : "+catalogDTO);
        list.add(catalogDTO);
        return ResponseEntity.status(HttpStatus.OK).body(catalogDTO);
    }

    @PostConstruct
    private void init(){
        list.add(CatalogDTO.builder().id(1).catalogType("electronics").price(15000).build());
        list.add(CatalogDTO.builder().id(2).catalogType("electronics").price(25000).build());
        list.add(CatalogDTO.builder().id(3).catalogType("garments").price(5000).build());
        list.add(CatalogDTO.builder().id(4).catalogType("electronics").price(15000).build());
        list.add(CatalogDTO.builder().id(5).catalogType("garments").price(5000).build());
        list.add(CatalogDTO.builder().id(6).catalogType("electronics").price(5000).build());
        list.add(CatalogDTO.builder().id(7).catalogType("garments").price(35000).build());
    }
}