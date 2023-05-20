package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto add(int userId, ItemDto itemDto) {
        userRepository.checkIsExist(userId);
        Item item = ItemMapper.mapToItem(itemDto, userId);
        return ItemMapper.mapToItemDto(itemRepository.add(item, userId));
    }

    @Override
    public ItemDto get(int itemId) {
        itemRepository.checkItemExist(itemId);
        return ItemMapper.mapToItemDto(itemRepository.get(itemId));
    }

    @Override
    public List<ItemDto> getAll(int userId) {
        userRepository.checkIsExist(userId);
        return itemRepository.getAllByOwner(userId)
                .stream()
                .map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.search(text)
                    .stream()
                    .map(ItemMapper::mapToItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        userRepository.checkIsExist(userId);
        itemRepository.checkItemExist(itemId);
        itemRepository.checkItemByUser(userId);
        Item item = ItemMapper.mapToItem(itemDto, userId);
        return ItemMapper.mapToItemDto(itemRepository.update(item, itemId, userId));
    }
}
