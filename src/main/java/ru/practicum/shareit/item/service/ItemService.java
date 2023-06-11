package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;

import java.util.List;

public interface ItemService {
    ItemDto add(int userId, ItemDto itemDto);

    ItemDtoBooking get(int itemId, int userId);

    List<ItemDtoBooking> getAll(int userId);

    List<ItemDto> searchItem(String text);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    CommentDto addComment(CommentDto commentDto, int itemId, int userId);
}
