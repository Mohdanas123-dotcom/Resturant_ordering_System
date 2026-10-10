package com.restaurant.rms.config;

import com.restaurant.rms.model.MenuItem;
import com.restaurant.rms.model.RestaurantTable;
import com.restaurant.rms.repository.MenuItemRepository;
import com.restaurant.rms.repository.RestaurantTableRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Adds sample data on first run only. */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(MenuItemRepository menu, RestaurantTableRepository tables) {
        return args -> {
            if (menu.count() == 0) {
                menu.saveAll(List.of(
                        new MenuItem("Garlic bread", "Starters", new BigDecimal("4.50")),
                        new MenuItem("Tomato soup", "Starters", new BigDecimal("5.00")),
                        new MenuItem("Margherita pizza", "Mains", new BigDecimal("11.00")),
                        new MenuItem("Grilled salmon", "Mains", new BigDecimal("18.50")),
                        new MenuItem("Vegetable pasta", "Mains", new BigDecimal("12.00")),
                        new MenuItem("Chocolate brownie", "Desserts", new BigDecimal("6.00")),
                        new MenuItem("Lemonade", "Drinks", new BigDecimal("3.50")),
                        new MenuItem("Cold coffee", "Drinks", new BigDecimal("4.50"))));
            }
            if (tables.count() == 0) {
                for (int i = 1; i <= 8; i++) {
                    tables.save(new RestaurantTable(i, i <= 4 ? 2 : (i <= 7 ? 4 : 8)));
                }
            }
        };
    }
}
