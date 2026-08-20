package com.order.service.dto;

import java.util.List;

public record BulkOrderRequest(

        String uploadedBy,

        List<CreateOrderRequest> orders

) {
}
