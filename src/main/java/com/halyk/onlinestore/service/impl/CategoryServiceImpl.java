package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.dto.category.response.CategoryResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import com.halyk.onlinestore.mapper.CategoryMapper;
import com.halyk.onlinestore.repository.CategoryRepository;
import com.halyk.onlinestore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getById(String id) {
        try {
            UUID categoryId = UUID.fromString(id);
            return categoryRepository.findById(categoryId)
                    .map(categoryMapper::toResponse)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Category not found");
        }
    }
}
