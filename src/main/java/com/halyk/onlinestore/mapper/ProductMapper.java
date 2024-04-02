package com.halyk.onlinestore.mapper;

import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.mapper.base.Mapper;
import com.halyk.onlinestore.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<Product, ProductResponse> {

    @Override
    public ProductResponse toResponse(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .category(entity.getCategory().getName())
                .build();
    }
}
