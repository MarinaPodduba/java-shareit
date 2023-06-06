package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.StateBookingEnum;
import ru.practicum.shareit.booking.dto.AddNewBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnknownStateException;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.OWNER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody AddNewBookingDto addNewBookingDto,
                                 @RequestHeader(OWNER_ID) int userId) {
        addNewBookingDto.setUserId(userId);
        BookingDto bookingDto = service.add(addNewBookingDto);
        log.info("Получен запрос POST /bookings. Владелец вещи {}", userId);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedOrRejectedBooking(@Valid @PathVariable int bookingId,
                                                @RequestHeader(OWNER_ID) int userId,
                                                @RequestParam boolean approved) {
        log.info("Получен запрос PATCH /bookings/{} (Подтверждение или отклонение запроса на бронирование)", bookingId);
        return service.approvedOrRejectedBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getByIdBooking(@PathVariable int bookingId,
                                     @RequestHeader(OWNER_ID) int userId) {
        log.info("Получен запрос GET /{}, от пользователя {}", bookingId, userId);
        return service.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsOfUser(@RequestHeader(OWNER_ID) int userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookings = service.getAllBookingsOfUser(userId, getBookingSearchState(state));
        log.info("Получен запрос GET /bookings ?state={}", state);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsOfOwner(@RequestHeader(OWNER_ID) int userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookings = service.getAllBookingsOfOwner(userId, getBookingSearchState(state));
        log.info("Получен запрос GET /bookings/owner?state={}", state);
        return bookings;
    }

    private StateBookingEnum getBookingSearchState(String state) {
        try {
            return StateBookingEnum.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(state);
        }
    }
}
