package ru.practicum.server.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.model.dto.CategoryDto;
import ru.practicum.server.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody @Valid CategoryDto catDto) {
        log.info("POST /admin/categories <- {}", catDto);
        return categoryService.create(catDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("DELETE /admin/categories/{}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    public Category patch(@RequestBody @Valid CategoryDto catDto, @PathVariable long catId) {
        log.info("PATCH /admin/categories/{} <- {}", catId, catDto);
        return categoryService.patch(catId, catDto);
    }
}
