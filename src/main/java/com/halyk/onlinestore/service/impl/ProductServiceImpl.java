package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.request.ProductUpdateRequest;
import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import com.halyk.onlinestore.mapper.ProductMapper;
import com.halyk.onlinestore.model.Category;
import com.halyk.onlinestore.model.Product;
import com.halyk.onlinestore.repository.CategoryRepository;
import com.halyk.onlinestore.repository.ProductRepository;
import com.halyk.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
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

    private static final String DEFAULT_SORT_BY = "createdAt";

    @Override
    public List<ProductResponse> getAll(Integer page, Integer size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Product> products;

        try {
            products = productRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            pageable = PageRequest.of(page, size, Sort.by(DEFAULT_SORT_BY));
            products = productRepository.findAll(pageable);
        }

        return products.map(productMapper::toResponse).toList();
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

    @Override
    @Transactional
    public void update(String id, ProductUpdateRequest request) {
        try {
            UUID productId = UUID.fromString(id);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            UUID categoryId = UUID.fromString(request.getCategoryId());
            Category newCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            product.setTitle(request.getTitle());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setCategory(newCategory);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        try {
            UUID productId = UUID.fromString(id);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            productRepository.delete(product);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Product not found");
        }
    }
}
