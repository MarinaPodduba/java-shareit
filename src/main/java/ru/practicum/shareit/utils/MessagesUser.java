package ru.practicum.shareit.utils;

public final class MessagesUser {
    public static final String ADD_USER = "Получен запрос POST /users";
    public static final String UPDATE_USER = "Получен запрос PATCH /users/{}";
    public static final String GET_USER = "Получен запрос GET /users/{}";
    public static final String DELETE_USER = "Получен запрос DELETE /users/{}";
    public static final String DEL_ALL_USERS = "Получен запрос GET /users";
    public static final String NOT_FOUND_USER = "Пользователь с id %d не найден";
    public static final String CHECK_EMAIL = "Пользователь с email: %s уже существует";
    public static final String ADD_REPOSITORY = "Успешно создан пользователь: {}";
    public static final String UPDATE_REPOSITORY = "Обновление пользователя прошло успешно";
    public static final String GET_REPOSITORY = "Получен пользователь с id {}";
    public static final String GET_ALL_USERS = "Успешно получены все пользователи";
    public static final String DELETE_USER_REPOSITORY = "Успешно удален пользователь с id {}";
}