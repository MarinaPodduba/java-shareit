package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

import static ru.practicum.shareit.utils.MessagesUser.*;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int countId = 0;

    @Override
    public User add(User user) {
        checkMail(user);
        user.setId(++countId);
        users.put(countId, user);
        emails.add(user.getEmail());
        log.info(ADD_REPOSITORY, user);
        return user;
    }

    @Override
    public User update(int userId, User user) {
        checkIsExist(userId);
        user.setId(userId);
        User prevUser = users.get(userId);
        if (user.getName() != null) {
            prevUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(prevUser.getEmail())) {
                checkMail(user);
                emails.remove(prevUser.getEmail());
                emails.add(user.getEmail());
            }
            prevUser.setEmail(user.getEmail());
        }
        log.info(UPDATE_REPOSITORY);
        return users.get(userId);
    }

    @Override
    public User get(int userId) {
        checkIsExist(userId);
        log.info(GET_REPOSITORY, userId);
        return users.get(userId);
    }

    @Override
    public void delete(int userId) {
        checkIsExist(userId);
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
        log.info(DELETE_USER_REPOSITORY, userId);
    }

    @Override
    public List<User> getAll() {
        log.info(GET_ALL_USERS);
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkIsExist(int id) {
        if (users.containsKey(id)) {
            return true;
        } else {
            throw new NotFoundException(String.format(NOT_FOUND_USER, id));
        }
    }

    @Override
    public void checkMail(User checkUser) {
        for (User user : users.values()) {
            if (checkUser.getEmail().equals(user.getEmail())) {
                throw new ValidationException(String.format(CHECK_EMAIL, user.getEmail()));
            }
        }
    }
}
