package ru.practicum.controller.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoryService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto dto) {
        log.trace("Добавление категории");
        return categoryService.addCategory(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        log.trace("Удаление категории");
        categoryService.deleteCategory(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Integer id,
                                      @Valid @RequestBody CategoryDto dto) {
        log.trace("Обновление категории");
        return categoryService.updateCategory(id, dto);
    }
}
