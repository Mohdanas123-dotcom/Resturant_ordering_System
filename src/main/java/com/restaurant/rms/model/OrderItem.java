package com.restaurant.rms.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Name and price are copied at order time, so later menu edits never rewrite history. */
@Entity
@Table(name = "order_item", indexes = @Index(name = "idx_item_order", columnList = "order_id"))
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    private Long menuItemId;
    private String itemName;
    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public OrderItem() {}

    public OrderItem(CustomerOrder order, Long menuItemId, String itemName, int quantity, BigDecimal unitPrice) {
        this.order = order; this.menuItemId = menuItemId; this.itemName = itemName;
        this.quantity = quantity; this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public CustomerOrder getOrder() { return order; }
    public Long getMenuItemId() { return menuItemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
