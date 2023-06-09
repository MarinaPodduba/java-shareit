package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.StatusBookingEnum;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto add(int userId, ItemDto itemDto) {
        User user = checkUser(userId);
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, user));
        itemDto.setId(item.getId());
        return itemDto;
    }

    @Override
    public ItemDtoBooking get(int itemId, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещь под id %d не найден", itemId)));
        List<Comment> comments = commentRepository.findCommentsByItemIdOrderByCreatedAsc(item.getId());
        return ItemMapper.toItemBookingDto(item,
                getLastBooking(item.getId(), userId),
                getNextBooking(item.getId(), userId),
                comments);
    }

    @Override
    public List<ItemDtoBooking> getAll(int userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        Map<Integer, Item> itemMap = itemRepository.findByOwnerId(userId)
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        Map<Integer, List<Booking>> bookingMap = bookingRepository.findByItemIdIn(itemMap.keySet(),
                        Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        Map<Integer, List<Comment>> comments = commentRepository.findByItemIdIn(itemMap.keySet(),
                        Sort.by(Sort.Direction.ASC, "created")).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));

        return items.stream()
                .map(item -> ItemMapper.toItemBookingDto(
                        item,
                        getAllLastBooking(bookingMap.getOrDefault(item.getId(), null)),
                        getAllNextBooking(bookingMap.getOrDefault(item.getId(), null)),
                        comments.getOrDefault(item.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                    .stream()
                    .map(ItemMapper::mapToItemDto)
                    .collect(toList());
        }
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        checkUser(userId);
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь под id %d не найден", itemId)));
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(updateItem));
    }

    private User checkUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, int userId) {
        User user = userRepository.findById(userId).stream().findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь под id %d не найден", itemId)));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId,
                StatusBookingEnum.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException(String.format("Вещь под id %d не бралась вами в аренду", itemId));
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        commentRepository.save(comment);
        return CommentMapper.mapToDto(comment);
    }

    private BookingShortDto getLastBooking(int itemId, int userId) {
        PageRequest page = PageRequest.of(0, 1);
        LocalDateTime now = LocalDateTime.now();
        List<BookingShortDto> lastList = bookingRepository.getLastBooking(itemId, userId,
                StatusBookingEnum.APPROVED, now, page);
        if (lastList.size() > 0) {
            return lastList.get(0);
        }
        return null;
    }

    private BookingShortDto getNextBooking(int itemId, int userId) {
        PageRequest page = PageRequest.of(0, 1);
        LocalDateTime now = LocalDateTime.now();
        List<BookingShortDto> nextList = bookingRepository.getNextBooking(itemId, userId,
                StatusBookingEnum.APPROVED, now, page);
        if (nextList.size() > 0) {
            return nextList.get(0);
        }
        return null;
    }

    private BookingShortDto getAllNextBooking(List<Booking> booking) {
        if (booking == null || booking.isEmpty()) {
            return null;
        }
        return booking.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .findFirst()
                .map(BookingMapper::toBookingShortDto)
                .orElse(null);
    }

    private BookingShortDto getAllLastBooking(List<Booking> booking) {
        if (booking == null || booking.isEmpty()) {
            return null;
        }
        return booking.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .reduce((booking1, booking2) -> booking2)
                .map(BookingMapper::toBookingShortDto)
                .orElse(null);
    }
}