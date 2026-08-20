package com.order.service.repository;

import com.order.service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByCustomerNameAndProductName(
            String customerName,
            String productName
    );

    List<Order> findByCustomerName(String customerName);
    List<Order> findByProductName(String productName);
}

