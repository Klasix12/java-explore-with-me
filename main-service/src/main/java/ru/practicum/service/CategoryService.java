package ru.practicum.service;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int id);

    CategoryDto addCategory(NewCategoryDto dto);

    void deleteCategory(int id);

    CategoryDto updateCategory(int id, CategoryDto dto);
}
