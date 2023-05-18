package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.MessagesUser.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info(ADD_USER);
        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(userService.add(user));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @PathVariable int userId,
                              @RequestBody UserDto userDto) {
        log.info(UPDATE_USER, userId);
        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(userService.update(userId, user));
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable int userId) {
        log.info(GET_USER, userId);
        return UserMapper.mapToUserDto(userService.get(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info(DELETE_USER, userId);
        userService.delete(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info(DEL_ALL_USERS);
        return userService.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}
