package com.halyk.onlinestore.controller;

import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.dto.category.response.CategoryResponse;
import com.halyk.onlinestore.dto.product.request.ProductCreationRequest;
import com.halyk.onlinestore.dto.product.request.ProductUpdateRequest;
import com.halyk.onlinestore.dto.product.response.ProductResponse;
import com.halyk.onlinestore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Viewing and managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Request for a list of products",
            description = """
                        Secured by JWT token.
                        Has 3 parameter for pagination and sorting: `page`, `size`, and `sortBy`.
                        If invalid `sortBy` is passed, default value is used.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(responseCode = "401",
                    description = """
                            If an incorrect JWT is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        return ResponseEntity.ok(productService.getAll(page, size, sortBy));
    }

    @Operation(
            summary = "Request for a product by ID",
            description = "Secured by JWT token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            If an incorrect JWT is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            If product is not found by provided ID, the service returns
                            a general error response with `status = 404` and `message = Product not found`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(
            summary = "Request for a creation of product",
            description = "Secured by JWT token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400",
                    description = """
                            If fields `title`, `price`, `description`, `categoryId` are blank, then corresponding response
                            with invalid field messages is returned.
                                                        
                            If field `price` is less than 0, then corresponding response
                            with invalid field messages is returned.
                                                        
                            If field `categoryId` is not UUID, then corresponding response
                            with invalid field messages is returned.
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            If an incorrect JWT is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            If category is not found by provided ID, the service returns
                            a general error response with `status = 404` and `message = Category not found`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid ProductCreationRequest request
    ) {
        productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Request for an update of product",
            description = "Secured by JWT token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400",
                    description = """
                            If fields `title`, `price`, `description`, `categoryId` are blank, then corresponding response
                            with invalid field messages is returned.
                                                        
                            If field `price` is less than 0, then corresponding response
                            with invalid field messages is returned.
                                                        
                            If field `categoryId` is not UUID, then corresponding response
                            with invalid field messages is returned.
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            If an incorrect JWT is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            If category is not found by provided ID, the service returns
                            a general error response with `status = 404` and `message = Category not found`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            If product is not found by provided ID, the service returns
                            a general error response with `status = 404` and `message = Product not found`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable String id,
            @RequestBody @Valid ProductUpdateRequest request
    ) {
        productService.update(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Request for a deletion of product",
            description = "Secured by JWT token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            If an incorrect JWT is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            If product is not found by provided ID, the service returns
                            a general error response with `status = 404` and `message = Product not found`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
