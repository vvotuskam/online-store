package com.halyk.onlinestore.service;

import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.request.ProductUpdateRequest;
import com.halyk.onlinestore.dto.product.response.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAll(Integer page, Integer size, String sortBy);

    ProductResponse getById(String id);

    void create(ProductCreationRequest request);

    void update(String id, ProductUpdateRequest request);

    void delete(String id);
}
