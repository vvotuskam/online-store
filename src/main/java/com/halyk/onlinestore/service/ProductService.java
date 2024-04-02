package com.halyk.onlinestore.service;

import com.halyk.onlinestore.dto.product.response.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAll();

    ProductResponse getById(String id);
}
