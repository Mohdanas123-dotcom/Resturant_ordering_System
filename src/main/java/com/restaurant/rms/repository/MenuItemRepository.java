package com.restaurant.rms.repository;

import com.restaurant.rms.model.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIgnoreCaseOrderByNameAsc(String category);
    List<MenuItem> findAllByOrderByCategoryAscNameAsc();
}
