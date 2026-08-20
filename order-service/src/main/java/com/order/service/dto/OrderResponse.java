package com.order.service.dto;

import com.order.service.entity.Order;
import com.order.service.entity.OrderStatus;
import com.order.service.entity.ProductStatus;

import java.math.BigDecimal;

public record OrderResponse(Long id, String customerName, String productName,
                            int quantity, BigDecimal price, OrderStatus status, ProductStatus productStatus) {}
