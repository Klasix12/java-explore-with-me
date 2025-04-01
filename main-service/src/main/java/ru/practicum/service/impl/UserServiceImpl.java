package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        log.info("Получение пользователей по id: {}", ids);
        if (ids == null || ids.isEmpty()) {
            return UserMapper.toDto(repository.findAll(PageRequest.of(from, size)));
        }
        return UserMapper.toDto(repository.findAllByIdIn(ids, PageRequest.of(from, size)));
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest user) {
        log.info("Добавление пользователя: {}", user);
        return UserMapper.toDto(repository.save(UserMapper.toEntity(user)));
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        log.info("Удаление пользователя. id: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        repository.deleteById(id);
    }
}
