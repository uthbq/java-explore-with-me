package ru.practicum.server.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompilationDtoPatch {
    private Set<Long> events;
    private Boolean pinned = false;
    @Length(min = 1, max = 50)
    private String title;
}
