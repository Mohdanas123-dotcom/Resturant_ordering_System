package com.restaurant.rms.controller;

import com.restaurant.rms.dto.OrderRequest;
import com.restaurant.rms.dto.OrderResponse;
import com.restaurant.rms.dto.StatusRequest;
import com.restaurant.rms.model.OrderStatus;
import com.restaurant.rms.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    /** GET /api/orders?active=true (default) or ?active=false&size=30 for recent history. */
    @GetMapping
    public List<OrderResponse> list(@RequestParam(defaultValue = "true") boolean active,
                                    @RequestParam(defaultValue = "30") int size) {
        return service.list(active, size);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) { return service.create(request); }

    @PatchMapping("/{id}/status")
    public OrderResponse status(@PathVariable Long id, @Valid @RequestBody StatusRequest body) {
        return service.updateStatus(id, OrderStatus.valueOf(body.status()));
    }
}
