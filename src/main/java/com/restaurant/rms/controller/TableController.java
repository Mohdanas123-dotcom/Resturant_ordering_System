package com.restaurant.rms.controller;

import com.restaurant.rms.dto.StatusRequest;
import com.restaurant.rms.model.RestaurantTable;
import com.restaurant.rms.model.TableStatus;
import com.restaurant.rms.service.TableService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService service;

    public TableController(TableService service) { this.service = service; }

    @GetMapping
    public List<RestaurantTable> list() { return service.list(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantTable create(@Valid @RequestBody RestaurantTable table) { return service.create(table); }

    @PatchMapping("/{id}/status")
    public RestaurantTable status(@PathVariable Long id, @Valid @RequestBody StatusRequest body) {
        return service.setStatus(id, TableStatus.valueOf(body.status()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
