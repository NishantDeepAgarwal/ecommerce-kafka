package com.order.service.dto;

public record InactiveOrder(
        CreateOrderRequest order,
        String reason
) {}
