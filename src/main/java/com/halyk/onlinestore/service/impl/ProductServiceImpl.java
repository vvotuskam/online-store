package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import com.halyk.onlinestore.mapper.ProductMapper;
import com.halyk.onlinestore.model.Category;
import com.halyk.onlinestore.model.Product;
import com.halyk.onlinestore.repository.CategoryRepository;
import com.halyk.onlinestore.repository.ProductRepository;
import com.halyk.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .orElseThrow(() -> new NotFoundException("Product not found"));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    @Transactional
    public void create(ProductCreationRequest request) {
        UUID categoryId = UUID.fromString(request.getCategoryId());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = Product.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(category)
                .build();

        productRepository.save(product);
    }


}
