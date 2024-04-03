package com.halyk.onlinestore.service;

import com.halyk.onlinestore.dto.category.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAll();

    CategoryResponse getById(String id);
}
