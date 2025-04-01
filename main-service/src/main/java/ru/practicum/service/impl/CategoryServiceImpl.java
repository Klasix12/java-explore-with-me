package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.CategoryNotEmptyException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Получение категорий. from: {}, size: {}", from, size);
        return CategoryMapper.toDto(repository.findAllBy(PageRequest.of(from, size)));
    }

    @Override
    public CategoryDto getCategory(int id) {
        log.info("Получение категории. id: {}", id);
        return CategoryMapper.toDto(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto dto) {
        log.info("Добавление категории: {}", dto);
        return CategoryMapper.toDto(repository.save(CategoryMapper.toEntity(dto)));
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        log.info("Удаление категории. id: {}", id);
        findByIdOrThrow(id);
        if (eventRepository.existsById(id)) {
            throw new CategoryNotEmptyException("Существуют события, связанные с категорией c id " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(int id, CategoryDto dto) {
        log.info("Обновление категории. id: {}", id);
        Category category = findByIdOrThrow(id);
        category.setName(dto.getName());
        return CategoryMapper.toDto(category);
    }

    private Category findByIdOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена"));
    }
}
