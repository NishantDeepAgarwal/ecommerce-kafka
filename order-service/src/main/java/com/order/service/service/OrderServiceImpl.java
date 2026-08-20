package com.order.service.service;

import com.order.service.dto.*;
import com.order.service.entity.Order;
import com.order.service.entity.OrderStatus;
import com.order.service.entity.ProductStatus;
import com.order.service.exception.DuplicateOrderException;
import com.order.service.exception.OrderNotFoundException;
import com.order.service.repository.OrderRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    public OrderServiceImpl (OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) throws OrderNotFoundException, DuplicateOrderException {

        boolean isDuplicate = orderRepository.existsByCustomerNameAndProductName(
                request.customerName(),
                request.productName()
        );

        if(request.quantity()<=0){
            throw new OrderNotFoundException("Quantity should be greater than zero.");
        }
        if(request.price().signum()<=0){
            throw new OrderNotFoundException("Price should be greater than zero.");
        }

        if(isDuplicate){
            throw new DuplicateOrderException("Duplicate Order");
        }

        Order requestOrderData = Order.builder()
                .customerName(request.customerName())
                .productName(request.productName())
                .quantity(request.quantity())
                .price(request.price())
                .status(OrderStatus.CREATED)
                .productStatus(ProductStatus.ACTIVE)
                .internalAuditUser("SELF")
                .version(1)
                .createdTime(LocalDateTime.now())
                .build();
        Order savedOrder = orderRepository.save(requestOrderData);

        return new OrderResponse(savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getPrice(), savedOrder.getStatus(), savedOrder.getProductStatus());
    }

    @Override
    public BulkOrderResponse bulkCreateOrders(BulkOrderRequest request) {

        List<OrderResponse> insertedOrders = new ArrayList<>();
        List<RejectedOrder> rejectedOrders = new ArrayList<>();
        List<DuplicateOrder> duplicateOrders = new ArrayList<>();

        String batchId = UUID.randomUUID().toString();

        for(CreateOrderRequest orderRequest : request.orders()) {

            if(orderRequest.customerName()==null || orderRequest.customerName().isBlank()){
                rejectedOrders.add(new RejectedOrder(orderRequest,"Invalid Customer name"));
                continue;
            }
            if(orderRequest.productName()==null || orderRequest.productName().isBlank()){
                rejectedOrders.add(new RejectedOrder(orderRequest,"Invalid Product name"));
                continue;
            }
            boolean isDuplicate = orderRepository.existsByCustomerNameAndProductName(
                    orderRequest.customerName(),
                    orderRequest.productName()
            );
            if(isDuplicate){
                duplicateOrders.add(new DuplicateOrder(orderRequest,"Duplicate Order"));
                continue;
            }
            if (orderRequest.quantity() <= 0) {
                rejectedOrders.add(
                        new RejectedOrder(
                                orderRequest,
                                "Quantity should be greater than zero."
                        )
                );
                continue;
            }

            if (orderRequest.price().signum() <= 0) {
                rejectedOrders.add(
                        new RejectedOrder(
                                orderRequest,
                                "Price should be greater than zero."
                        )
                );
                continue;
            }

            Order order = Order.builder()
                    .customerName(orderRequest.customerName())
                    .productName(orderRequest.productName())
                    .quantity(orderRequest.quantity())
                    .price(orderRequest.price())
                    .status(OrderStatus.CREATED)
                    .productStatus(ProductStatus.ACTIVE)
                    .internalAuditUser(request.uploadedBy())
                    .version(1)
                    .createdTime(LocalDateTime.now())
                    .build();

            Order savedOrder=orderRepository.save(order);

            insertedOrders.add(
                    new OrderResponse(
                            savedOrder.getId(),
                            savedOrder.getCustomerName(),
                            savedOrder.getProductName(),
                            savedOrder.getQuantity(),
                            savedOrder.getPrice(),
                            savedOrder.getStatus(),
                            savedOrder.getProductStatus()
                    )
            );
        }
        return new BulkOrderResponse(
                batchId,
                request.uploadedBy(),
                request.orders().size(),
                insertedOrders.size(),
                rejectedOrders.size(),
                duplicateOrders.size(),
                insertedOrders,
                rejectedOrders,
                duplicateOrders
        );
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return getOrderResponses(orders);
    }

    @Override
    public @Nullable List<OrderResponse> getOrdersByCustomerName(String customerName) {
        List<Order> orders = orderRepository.findByCustomerName(customerName);
        if(orders.isEmpty()) {
            return null;
        }
        return getOrderResponses(orders);
    }

    @Override
    public List<OrderResponse> getOrdersByProductName(String customerName) {
        return orderRepository.findByProductName(customerName).stream()
                .map(order -> new OrderResponse(order.getId(), order.getCustomerName(), order.getProductName(),
                        order.getQuantity(), order.getPrice(), order.getStatus(), order.getProductStatus()))
                .collect(Collectors.toList());
    }

    private List<OrderResponse> getOrderResponses(List<Order> orders) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders) {
            orderResponses.add(new OrderResponse(order.getId(),order.getCustomerName(),order.getProductName(),
                    order.getQuantity(),order.getPrice(),order.getStatus(),order.getProductStatus()));
        }
        return orderResponses;
    }
}
