package ru.practicum.server.category.service;

import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.model.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    Category create(CategoryDto catDto);

    void delete(long catId);

    Category patch(long catId, CategoryDto catDto);

    Collection<Category> getAll(int from, int size);

    Category get(long catId);
}
