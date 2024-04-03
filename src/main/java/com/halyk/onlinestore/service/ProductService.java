package com.halyk.onlinestore.service;

import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.response.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAll();

    ProductResponse getById(String id);

    void create(ProductCreationRequest request);
}
