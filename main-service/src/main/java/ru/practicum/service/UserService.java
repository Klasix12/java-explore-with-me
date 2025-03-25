package ru.practicum.service;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto addUser(NewUserRequest user);

    void deleteUser(int id);
}
