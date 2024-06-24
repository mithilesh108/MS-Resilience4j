package com.cts.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatalogDTO {

    private int id;
    private String catalogType;
    private int price;

}
