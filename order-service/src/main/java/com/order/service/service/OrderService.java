package com.order.service.service;

import com.order.service.dto.BulkOrderRequest;
import com.order.service.dto.BulkOrderResponse;
import com.order.service.dto.CreateOrderRequest;
import com.order.service.dto.OrderResponse;
import com.order.service.entity.Order;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    BulkOrderResponse bulkCreateOrders(BulkOrderRequest request);

    List<OrderResponse> getAllOrders();

    @Nullable List<OrderResponse> getOrdersByCustomerName(String customerName);
    List<OrderResponse> getOrdersByProductName(String productName);
}
