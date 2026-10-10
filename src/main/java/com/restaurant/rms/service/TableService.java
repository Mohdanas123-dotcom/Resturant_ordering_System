package com.restaurant.rms.service;

import com.restaurant.rms.exception.NotFoundException;
import com.restaurant.rms.model.RestaurantTable;
import com.restaurant.rms.model.TableStatus;
import com.restaurant.rms.repository.RestaurantTableRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final RestaurantTableRepository repo;

    public TableService(RestaurantTableRepository repo) { this.repo = repo; }

    public List<RestaurantTable> list() { return repo.findAllByOrderByNumberAsc(); }

    @Transactional
    public RestaurantTable create(RestaurantTable t) {
        t.setId(null);
        t.setStatus(TableStatus.AVAILABLE);
        return repo.saveAndFlush(t); // flush so a duplicate number fails here, not at commit
    }

    @Transactional
    public RestaurantTable setStatus(Long id, TableStatus status) {
        RestaurantTable t = find(id);
        t.setStatus(status);
        return t;
    }

    @Transactional
    public void delete(Long id) {
        repo.delete(find(id));
        repo.flush();
    }

    private RestaurantTable find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Table " + id + " not found"));
    }
}
