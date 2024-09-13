package ru.practicum.server.category.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.server.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static Category mapToCategory(CategoryDto catDto) {
        return new Category(null, catDto.getName());
    }
}
