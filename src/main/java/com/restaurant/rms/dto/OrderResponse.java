package com.restaurant.rms.dto;

import com.restaurant.rms.model.CustomerOrder;
import com.restaurant.rms.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long tableId,
        int tableNumber,
        OrderStatus status,
        Instant createdAt,
        BigDecimal total,
        List<Line> items) {

    public record Line(String name, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {}

    public static OrderResponse from(CustomerOrder o) {
        List<Line> lines = o.getItems().stream()
                .map(i -> new Line(i.getItemName(), i.getQuantity(), i.getUnitPrice(),
                        i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))))
                .toList();
        return new OrderResponse(o.getId(), o.getTable().getId(), o.getTable().getNumber(),
                o.getStatus(), o.getCreatedAt(), o.getTotal(), lines);
    }
}
