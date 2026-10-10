package com.restaurant.rms.dto;

import java.math.BigDecimal;

public record DashboardSummary(long activeOrders, long occupiedTables, long totalTables, BigDecimal todayRevenue) {}
