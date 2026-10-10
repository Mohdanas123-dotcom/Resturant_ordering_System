package com.restaurant.rms.repository;

import com.restaurant.rms.model.RestaurantTable;
import com.restaurant.rms.model.TableStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findAllByOrderByNumberAsc();
    long countByStatus(TableStatus status);
}
