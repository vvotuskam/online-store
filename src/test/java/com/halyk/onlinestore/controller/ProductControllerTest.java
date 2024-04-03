package com.halyk.onlinestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.request.ProductUpdateRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Test
    @SneakyThrows
    void testGetAllWhenSortByIsInvalid() {
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "invalidField")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].price").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].category.id").exists())
                .andExpect(jsonPath("$[0].category.name").exists());
    }

    @Test
    @SneakyThrows
    void testGetAllWhenOk() {
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].price").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].category.id").exists())
                .andExpect(jsonPath("$[0].category.name").exists());
    }

    @Test
    @SneakyThrows
    void testGetByIdWhenIdIsNotUuid() {
        String id = "not-uuid";

        mockMvc.perform(get("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testGetByIdWhenNotFound() {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testGetByIdWhenOk() {
        UUID id = UUID.fromString("5defee46-97a1-49ee-9d3a-a35ee33b74e9");

        mockMvc.perform(get("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.category").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testCreateWhenBadRequest() {
        ProductCreationRequest request = new ProductCreationRequest();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Bad request"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors.length()").value(4));
    }

    @Test
    @SneakyThrows
    void testCreateWhenCategoryNotFound() {
        ProductCreationRequest request = new ProductCreationRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                UUID.randomUUID().toString()
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Category not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testCreateWhenOk() {
        ProductCreationRequest request = new ProductCreationRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                "f64d05d7-158e-46b7-abf7-5c2724760228"
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void testUpdateWhenBadRequest() {
        UUID id = UUID.randomUUID();
        ProductUpdateRequest request = new ProductUpdateRequest();

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Bad request"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors.length()").value(4));
    }

    @Test
    @SneakyThrows
    void testUpdateWhenIdIsNotUuid() {
        String id = "not-uuid";
        ProductUpdateRequest request = new ProductUpdateRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                UUID.randomUUID().toString()
        );

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testUpdateWhenProductNotFound() {
        UUID id = UUID.randomUUID();
        ProductUpdateRequest request = new ProductUpdateRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                UUID.randomUUID().toString()
        );

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testUpdateWhenCategoryNotFound() {
        UUID id = UUID.fromString("5defee46-97a1-49ee-9d3a-a35ee33b74e9");
        ProductUpdateRequest request = new ProductUpdateRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                UUID.randomUUID().toString()
        );

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Category not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testUpdateWhenOk() {
        UUID id = UUID.fromString("5defee46-97a1-49ee-9d3a-a35ee33b74e9");
        ProductUpdateRequest request = new ProductUpdateRequest(
                "Product",
                new BigDecimal(1500),
                "New Product",
                "f64d05d7-158e-46b7-abf7-5c2724760228"
        );

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void testDeleteWhenIdIsNotUuid() {
        String id = "not-uuid";

        mockMvc.perform(delete("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testDeleteWhenNotFound() {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void testDeleteWhenOk() {
        UUID id = UUID.fromString("5defee46-97a1-49ee-9d3a-a35ee33b74e9");

        mockMvc.perform(delete("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
