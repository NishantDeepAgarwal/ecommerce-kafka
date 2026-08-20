package com.order.service.dto;

public record DuplicateOrder(
        CreateOrderRequest order, String reason) {
}
