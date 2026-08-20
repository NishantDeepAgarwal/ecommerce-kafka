package com.order.service.dto;

public record RejectedOrder(CreateOrderRequest order, String reason) {
}