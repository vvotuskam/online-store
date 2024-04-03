package com.halyk.onlinestore.dto.product.response;

import com.halyk.onlinestore.dto.category.response.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID id;
    private String title;
    private BigDecimal price;
    private String description;
    private CategoryResponse category;
}
