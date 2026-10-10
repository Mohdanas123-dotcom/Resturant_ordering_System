package com.restaurant.rms.service;

import com.restaurant.rms.dto.DashboardSummary;
import com.restaurant.rms.dto.OrderRequest;
import com.restaurant.rms.dto.OrderResponse;
import com.restaurant.rms.exception.NotFoundException;
import com.restaurant.rms.model.*;
import com.restaurant.rms.repository.CustomerOrderRepository;
import com.restaurant.rms.repository.MenuItemRepository;
import com.restaurant.rms.repository.RestaurantTableRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CustomerOrderRepository orders;
    private final RestaurantTableRepository tables;
    private final MenuItemRepository menu;

    public OrderService(CustomerOrderRepository orders, RestaurantTableRepository tables, MenuItemRepository menu) {
        this.orders = orders;
        this.tables = tables;
        this.menu = menu;
    }

    public List<OrderResponse> list(boolean activeOnly, int size) {
        List<CustomerOrder> result = activeOnly
                ? orders.findByStatusInOrderByCreatedAtDesc(OrderStatus.ACTIVE)
                : orders.findAll(PageRequest.of(0, Math.min(Math.max(size, 1), 100),
                        Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
        return result.stream().map(OrderResponse::from).toList();
    }

    public OrderResponse get(Long id) {
        return OrderResponse.from(find(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest req) {
        RestaurantTable table = tables.findById(req.tableId())
                .orElseThrow(() -> new NotFoundException("Table " + req.tableId() + " not found"));

        // Merge duplicate lines, then load every menu item in ONE query.
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        req.items().forEach(l -> quantities.merge(l.menuItemId(), l.quantity(), Integer::sum));
        Map<Long, MenuItem> found = menu.findAllById(quantities.keySet()).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        CustomerOrder order = new CustomerOrder();
        order.setTable(table);
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : quantities.entrySet()) {
            MenuItem m = found.get(e.getKey());
            if (m == null) throw new NotFoundException("Menu item " + e.getKey() + " not found");
            if (!m.isAvailable()) throw new IllegalStateException(m.getName() + " is not available right now");
            order.addItem(new OrderItem(order, m.getId(), m.getName(), e.getValue(), m.getPrice()));
            total = total.add(m.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        }
        order.setTotal(total);
        table.setStatus(TableStatus.OCCUPIED);
        return OrderResponse.from(orders.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus next) {
        CustomerOrder order = find(id);
        if (order.getStatus().isTerminal()) {
            throw new IllegalStateException("Order #" + id + " is already " + order.getStatus());
        }
        order.setStatus(next);
        if (next.isTerminal()
                && !orders.existsByTableIdAndStatusIn(order.getTable().getId(), OrderStatus.ACTIVE)) {
            order.getTable().setStatus(TableStatus.AVAILABLE);
        }
        return OrderResponse.from(order);
    }

    public DashboardSummary dashboard() {
        var since = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        return new DashboardSummary(
                orders.countByStatusIn(OrderStatus.ACTIVE),
                tables.countByStatus(TableStatus.OCCUPIED),
                tables.count(),
                orders.revenueSince(OrderStatus.PAID, since));
    }

    private CustomerOrder find(Long id) {
        return orders.findById(id).orElseThrow(() -> new NotFoundException("Order " + id + " not found"));
    }
}
