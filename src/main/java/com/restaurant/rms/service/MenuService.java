package com.restaurant.rms.service;

import com.restaurant.rms.exception.NotFoundException;
import com.restaurant.rms.model.MenuItem;
import com.restaurant.rms.repository.MenuItemRepository;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuItemRepository repo;

    public MenuService(MenuItemRepository repo) { this.repo = repo; }

    /** The menu is read far more often than it changes, so reads are cached and writes evict. */
    @Cacheable(value = "menu", key = "#category ?: 'all'")
    public List<MenuItem> list(String category) {
        return (category == null || category.isBlank())
                ? repo.findAllByOrderByCategoryAscNameAsc()
                : repo.findByCategoryIgnoreCaseOrderByNameAsc(category);
    }

    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public MenuItem create(MenuItem item) {
        item.setId(null);
        return repo.save(item);
    }

    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public MenuItem update(Long id, MenuItem in) {
        MenuItem m = find(id);
        m.setName(in.getName());
        m.setCategory(in.getCategory());
        m.setPrice(in.getPrice());
        m.setAvailable(in.isAvailable());
        return m;
    }

    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public MenuItem setAvailable(Long id, boolean available) {
        MenuItem m = find(id);
        m.setAvailable(available);
        return m;
    }

    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public void delete(Long id) {
        repo.delete(find(id));
    }

    private MenuItem find(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Menu item " + id + " not found"));
    }
}
