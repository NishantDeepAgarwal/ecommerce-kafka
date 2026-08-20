package com.order.service.dto;

import java.util.List;

public record BulkOrderResponse(
        String batchId,

        String uploadedBy,

        Integer totalRecords,

        Integer insertedCount,

        Integer rejectedCount,

        Integer duplicateCount,

        List<OrderResponse> insertedOrders,

        List<RejectedOrder> rejectedOrders,

        List<DuplicateOrder> duplicateOrders
) {
}
