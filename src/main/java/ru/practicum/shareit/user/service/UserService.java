package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(int userId, User user);

    User get(int userId);

    void delete(int userId);

    List<User> getAll();
}
