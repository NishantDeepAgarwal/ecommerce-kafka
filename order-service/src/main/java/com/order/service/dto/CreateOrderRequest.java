package com.order.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderRequest(

        @NotBlank
        String customerName,

        @NotBlank
        String productName,

        @Positive
        Integer quantity,

        @Positive
        BigDecimal price
) {}
