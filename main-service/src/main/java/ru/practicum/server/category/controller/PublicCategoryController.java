package ru.practicum.server.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.service.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<Category> getAll(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /categories <-");
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public Category getById(@PathVariable long catId) {
        log.info("GET /categories/{} <-", catId);
        return categoryService.get(catId);
    }
}
