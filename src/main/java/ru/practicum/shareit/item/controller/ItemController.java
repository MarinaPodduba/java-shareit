package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.OWNER_ID;
import static ru.practicum.shareit.utils.MessagesItem.*;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto addItem(@RequestHeader(OWNER_ID) int userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info(ADD_ITEM);
        return service.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(OWNER_ID) int userId,
                              @PathVariable int itemId,
                              @RequestBody ItemDto itemDto) {
        log.info(UPDATE_ITEM, userId);
        return service.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        log.info(GET_ITEM, itemId);
        return service.get(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(OWNER_ID) int userId) {
        log.info(GET_ALL_ITEMS);
        return service.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info(SEARCH_ITEM, text);
        return service.searchItem(text);
    }
}
