package com.order.service.controller;

import com.order.service.dto.*;
import com.order.service.entity.Order;
import com.order.service.entity.UserResponse;
import com.order.service.service.OrderService;
import com.order.service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
    @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkOrderResponse> bulkUpload(
            @RequestBody BulkOrderRequest request) {
        return ResponseEntity.ok(orderService.bulkCreateOrders(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerName(@PathVariable String customerName) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerName(customerName));
    }

    @GetMapping("/product/{productName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByProductName(@PathVariable String productName) {
        return ResponseEntity.ok(orderService.getOrdersByProductName(productName));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        registerUserRequest.setRole(Role.USER);
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerByAdmin(@RequestBody RegisterUserRequest registerUserRequest){
        UserResponse userResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok("admin user registered successfully:");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegisterUserRequest registerUserRequest) {
        String token=userService.generateTokenFromRequest(registerUserRequest);
        return ResponseEntity.ok(token);
    }
}
