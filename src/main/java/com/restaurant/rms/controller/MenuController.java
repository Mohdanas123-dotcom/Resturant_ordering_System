package com.restaurant.rms.controller;

import com.restaurant.rms.dto.StatusRequest;
import com.restaurant.rms.model.MenuItem;
import com.restaurant.rms.service.MenuService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) { this.service = service; }

    @GetMapping
    public List<MenuItem> list(@RequestParam(required = false) String category) {
        return service.list(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItem create(@Valid @RequestBody MenuItem item) { return service.create(item); }

    @PutMapping("/{id}")
    public MenuItem update(@PathVariable Long id, @Valid @RequestBody MenuItem item) {
        return service.update(id, item);
    }

    /** Body: {"status":"true"} or {"status":"false"} */
    @PatchMapping("/{id}/availability")
    public MenuItem availability(@PathVariable Long id, @Valid @RequestBody StatusRequest body) {
        return service.setAvailable(id, Boolean.parseBoolean(body.status()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
