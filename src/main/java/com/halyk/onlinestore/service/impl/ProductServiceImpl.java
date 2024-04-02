package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import com.halyk.onlinestore.mapper.ProductMapper;
import com.halyk.onlinestore.repository.CategoryRepository;
import com.halyk.onlinestore.repository.ProductRepository;
import com.halyk.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(String id) {
        try {
            return productRepository.findById(UUID.fromString(id))
                    .map(productMapper::toResponse)
                    .orElseThrow(NotFoundException::new);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException();
        }
    }
}
