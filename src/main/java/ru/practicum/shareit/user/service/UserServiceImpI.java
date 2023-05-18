package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpI implements UserService {
    private final UserRepository repository;

    @Override
    public User add(User user) {
        return repository.add(user);
    }

    @Override
    public User update(int userId, User user) {
        return repository.update(userId, user);
    }

    @Override
    public User get(int userId) {
        return repository.get(userId);
    }

    @Override
    public void delete(int userId) {
        repository.delete(userId);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
