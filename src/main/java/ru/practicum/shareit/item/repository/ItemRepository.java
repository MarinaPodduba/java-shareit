package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item add(Item item, int userId);

    Item get(int itemId);

    List<Item> getAllByOwner(int userId);

    List<Item> getAll();

    Item update(Item item, int itemId, int userId);

    List<Item> search(String text);

    void checkItemExist(int itemId);

    void checkItemByUser(int userId);
}
