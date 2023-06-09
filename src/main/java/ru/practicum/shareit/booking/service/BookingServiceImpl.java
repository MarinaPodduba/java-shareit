package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.StateBookingEnum;
import ru.practicum.shareit.booking.StatusBookingEnum;
import ru.practicum.shareit.booking.dto.AddNewBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto add(AddNewBookingDto addNewBookingDto, int userId) {
        Item item = itemRepository.findById(addNewBookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException(String.format("Вещь под id %d не найден", addNewBookingDto.getItemId())));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь под id %d не найден", addNewBookingDto.getUserId())));
        checkAdd(addNewBookingDto, user, item);
        Booking booking = bookingRepository.save(BookingMapper.mapToAddNewBooking(addNewBookingDto, item, user));
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto approvedOrRejectedBooking(int bookingId, int userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование под id %d не найден", bookingId)));
        if (!booking.getStatus().equals(StatusBookingEnum.WAITING)) {
            throw new ValidationException("Статус должен быть WAITING"); // тут должна быть 404 ошибка
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Выполнение данной операции доступно только владельцу вещи");
        }
        if (approved) {
            booking.setStatus(StatusBookingEnum.APPROVED);
        } else {
            booking.setStatus(StatusBookingEnum.REJECTED);
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto get(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование под id %d не найден", bookingId)));
        userRepository.existsById(userId);
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException(String.format("Бронирование под id %d не найден", bookingId));
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsOfUser(int userId, StateBookingEnum stateBookingEnum) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь под id %d не найден", userId));
        }
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        List<Booking> bookingsList = new ArrayList<>();
        try {
            switch (stateBookingEnum) {
                case ALL:
                    bookingsList.addAll(bookingRepository.findAllByBookerId(userId, sort));
                    break;
                case CURRENT:
                    bookingsList.addAll(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                                    LocalDateTime.now(), LocalDateTime.now(), sort));
                    break;
                case PAST:
                    bookingsList.addAll(bookingRepository.findAllByBookerIdAndEndIsBefore(userId,
                            LocalDateTime.now(), sort));
                    break;
                case FUTURE:
                    bookingsList.addAll(bookingRepository.findAllByBookerIdAndStartIsAfter(userId,
                            LocalDateTime.now(), sort));
                    break;
                case WAITING:
                    bookingsList.addAll(bookingRepository.findAllByBookerIdAndStatus(userId,
                            StatusBookingEnum.WAITING, sort));
                    break;
                case REJECTED:
                    bookingsList.addAll(bookingRepository.findAllByBookerIdAndStatus(userId,
                            StatusBookingEnum.REJECTED, sort));
                    break;
            }
            return bookingsList.stream()
                    .map(BookingMapper::mapToBookingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<BookingDto> getAllBookingsOfOwner(int userId, final StateBookingEnum stateBookingEnum) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь под id %d не найден", userId));
        }
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        List<Booking> bookingsList = new ArrayList<>();
        try {
            switch (stateBookingEnum) {
                case ALL:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerId(userId, sort));
                    break;
                case CURRENT:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId,
                                    LocalDateTime.now(), LocalDateTime.now(), sort));
                    break;
                case PAST:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerIdAndEndIsBefore(userId,
                            LocalDateTime.now(), sort));
                    break;
                case FUTURE:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerIdAndStartIsAfter(userId,
                            LocalDateTime.now(), sort));
                    break;
                case WAITING:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerIdAndStatus(userId,
                            StatusBookingEnum.WAITING, sort));
                    break;
                case REJECTED:
                    bookingsList.addAll(bookingRepository.findAllByItemOwnerIdAndStatus(userId,
                            StatusBookingEnum.REJECTED, sort));
                    break;
            }
            return bookingsList.stream()
                    .map(BookingMapper::mapToBookingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    private void checkAdd(AddNewBookingDto addNewBookingDto, User user, Item item) {
        if (addNewBookingDto.getStart().isAfter(addNewBookingDto.getEnd())) {
            throw new ValidationException("Дата окончания бронирования не может быть раньше даты начала бронирования");
        }
        if (addNewBookingDto.getStart().isEqual(addNewBookingDto.getEnd())) {
            throw new ValidationException("Дата окончания бронирования не может быть равна дате начала бронирования");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для брониварония");
        }
        if (user.getId() == (item.getOwner().getId())) {
            throw new NotFoundException("Владелец не может забронировать собственный товар");
        }
    }
}
