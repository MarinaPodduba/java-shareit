package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(int userId, ItemDto itemDto);

    ItemDto get(int itemId);

    List<ItemDto> getAll(int userId);

    List<ItemDto> searchItem(String text);

    ItemDto update(int userId, int itemId, ItemDto itemDto);
}
