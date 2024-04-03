package com.halyk.onlinestore.mapper;

import com.halyk.onlinestore.dto.category.response.CategoryResponse;
import com.halyk.onlinestore.mapper.base.Mapper;
import com.halyk.onlinestore.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<Category, CategoryResponse> {

    @Override
    public CategoryResponse toResponse(Category entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
