package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static ru.practicum.shareit.utils.MessagesItem.*;
import static ru.practicum.shareit.utils.MessagesUser.NOT_FOUND_USER;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Item>> itemsByUser = new HashMap<>();
    private int countId = 0;

    @Override
    public Item add(Item item, int userId) {
        item.setId(++countId);
        if (itemsByUser.containsKey(userId)) {
            itemsByUser.get(userId).add(item);
        } else {
            List<Item> newList = new ArrayList<>();
            newList.add(item);
            itemsByUser.put(userId, newList);
        }
        items.put(countId, item);
        log.info(ADD_REPOSITORY, item);
        return item;
    }

    @Override
    public Item get(int itemId) {
        log.info(GET_REPOSITORY, itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllByOwner(int userId) {
        List<Item> list = itemsByUser.get(userId);
        log.info(GET_ALL_BY_OWNER_REPOSITORY, userId);
        return list;
    }

    @Override
    public List<Item> getAll() {
        log.info(GET_ALL_ITEMS_REPOSITORY, items.values());
        return new ArrayList<>(items.values());
    }

    @Override
    public Item update(Item item, int itemId, int userId) {
        Item updatingItem = items.get(itemId);
        if (item.getName() != null) {
            updatingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatingItem.setAvailable(item.getAvailable());
        }
        for (Item userItem : itemsByUser.get(userId)) {
            if (userItem.getId() == itemId) {
                itemsByUser.get(userId).remove(userItem);
                itemsByUser.get(userId).add(updatingItem);
            }
        }
        items.replace(updatingItem.getId(), updatingItem);
        log.info(UPDATE_REPOSITORY);
        return updatingItem;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemsToResponse = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                if (item.getAvailable()) {
                    itemsToResponse.add(item);
                }
            }
        }
        return itemsToResponse;
    }

    @Override
    public void checkItemExist(int itemId) {
        Item item = items.get(itemId);
        if (item != null) {
            log.info(CHECK_ITEM, itemId);
        } else {
            throw new NotFoundException(String.format(NOT_FOUND_ITEM, itemId));
        }
    }

    @Override
    public void checkItemByUser(int userId) {
        if (!itemsByUser.containsKey(userId)) {
            throw new NotFoundException(String.format(NOT_FOUND_USER, userId));
        }
    }
}
