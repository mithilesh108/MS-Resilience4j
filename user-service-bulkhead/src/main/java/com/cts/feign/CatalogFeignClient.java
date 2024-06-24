package com.cts.feign;

import com.cts.dto.CatalogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATALOG-SERVICE")
public interface CatalogFeignClient {
    @GetMapping("/catalog/api/id/{id}")
    public CatalogDTO findById(@PathVariable("id") int userId);

    @GetMapping("/catalog/api/name/{name}")
    public CatalogDTO findByName(@PathVariable("name") String catalogType);
}
