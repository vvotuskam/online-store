package com.halyk.onlinestore.mapper;

import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.mapper.base.Mapper;
import com.halyk.onlinestore.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper implements Mapper<Product, ProductResponse> {

    private final CategoryMapper categoryMapper;

    @Override
    public ProductResponse toResponse(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .category(categoryMapper.toResponse(entity.getCategory()))
                .build();
    }
}
