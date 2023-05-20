package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User add(User user);

    User update(int userId, User user);

    User get(int userId);

    void delete(int userId);

    List<User> getAll();

    boolean checkIsExist(int id);

    void checkMail(User checkUser);
}
