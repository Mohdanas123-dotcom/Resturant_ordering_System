package com.restaurant.rms.controller;

import com.restaurant.rms.dto.DashboardSummary;
import com.restaurant.rms.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final OrderService service;

    public DashboardController(OrderService service) { this.service = service; }

    @GetMapping
    public DashboardSummary summary() { return service.dashboard(); }
}
