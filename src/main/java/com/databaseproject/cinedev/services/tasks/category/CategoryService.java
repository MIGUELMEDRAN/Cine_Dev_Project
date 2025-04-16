package com.databaseproject.cinedev.services.tasks.category;

import com.databaseproject.cinedev.models.task.Category;
import com.databaseproject.cinedev.repository.task.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void addCustomCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void removeCustomCategory(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name).orElse(null);
    }
}
