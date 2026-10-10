package com.restaurant.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_item", indexes = @Index(name = "idx_menu_category", columnList = "category"))
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Size(max = 50)
    private String category;

    @NotNull @DecimalMin("0.01")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private boolean available = true;

    public MenuItem() {}

    public MenuItem(String name, String category, BigDecimal price) {
        this.name = name; this.category = category; this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
