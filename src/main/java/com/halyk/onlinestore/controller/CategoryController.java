package com.halyk.onlinestore.controller;

import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.dto.category.response.CategoryResponse;
import com.halyk.onlinestore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Viewing categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Request for a list of categories",
            description = "Secured by JWT token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200"
            ),
            @ApiResponse(responseCode = "401",
                    description = """
                            If an incorrect username or an incorrect password is sent, the service returns
                            a general authorization error with `status = 401` and the following description of the reason in
                            the `message` field:
                                          
                              `Unauthorized`
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(
            summary = "Request for a category by ID",
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
                            If an incorrect username or an incorrect password is sent, the service returns
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
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }
}
