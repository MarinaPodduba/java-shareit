package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpI implements UserService {
    private final UserRepository repository;

    @Override
    public User add(User user) {
        return repository.save(user);
    }

    @Override
    public User update(int userId, User user) {
        User updateUser = repository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь под id %d не найден", userId)));
        updateUser.setId(userId);
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        return repository.save(updateUser);
    }

    @Override
    public User get(int userId) {
        return repository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь под id %d не найден", userId)));
    }

    @Override
    public void delete(int userId) {
        repository.deleteById(userId);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }
}
