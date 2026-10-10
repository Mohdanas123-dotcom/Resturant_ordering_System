package com.restaurant.rms.repository;

import com.restaurant.rms.model.CustomerOrder;
import com.restaurant.rms.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    // The table is fetched in the same query; items are loaded in batches (default_batch_fetch_size).
    @EntityGraph(attributePaths = "table")
    List<CustomerOrder> findByStatusInOrderByCreatedAtDesc(Collection<OrderStatus> statuses);

    @Override
    @EntityGraph(attributePaths = "table")
    Page<CustomerOrder> findAll(Pageable pageable);

    boolean existsByTableIdAndStatusIn(Long tableId, Collection<OrderStatus> statuses);

    long countByStatusIn(Collection<OrderStatus> statuses);

    @Query("select coalesce(sum(o.total), 0) from CustomerOrder o where o.status = :status and o.createdAt >= :since")
    BigDecimal revenueSince(@Param("status") OrderStatus status, @Param("since") Instant since);
}
