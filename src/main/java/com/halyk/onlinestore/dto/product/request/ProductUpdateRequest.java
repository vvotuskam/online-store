package com.halyk.onlinestore.dto.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    private String title;

    @NotNull
    @Min(value = 0)
    private BigDecimal price;

    @NotBlank
    private String description;

    @UUID
    @NotBlank
    private String categoryId;
}
