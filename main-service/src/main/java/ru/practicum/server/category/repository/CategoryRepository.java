package ru.practicum.server.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
