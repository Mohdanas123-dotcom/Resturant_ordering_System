package com.restaurant.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "restaurant_table",
       uniqueConstraints = @UniqueConstraint(name = "uk_table_number", columnNames = "number"))
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int number;

    @Min(1)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TableStatus status = TableStatus.AVAILABLE;

    public RestaurantTable() {}

    public RestaurantTable(int number, int capacity) {
        this.number = number; this.capacity = capacity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}
