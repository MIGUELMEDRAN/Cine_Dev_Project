package com.databaseproject.cinedev.repository.task;

import com.databaseproject.cinedev.models.base.User;
import com.databaseproject.cinedev.models.task.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findByUserAdminId(User user);
}
