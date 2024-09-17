package ru.practicum.server.category.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {
    @NotBlank
    @Length(min = 1, max = 50)
    private String name;
}
