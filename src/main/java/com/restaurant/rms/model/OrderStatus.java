package com.restaurant.rms.model;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    NEW, PREPARING, SERVED, PAID, CANCELLED;

    public static final Set<OrderStatus> ACTIVE = EnumSet.of(NEW, PREPARING, SERVED);

    public boolean isTerminal() {
        return this == PAID || this == CANCELLED;
    }
}
