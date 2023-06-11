package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.StateBookingEnum;
import ru.practicum.shareit.booking.dto.AddNewBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(AddNewBookingDto addNewBookingDto, int userId);

    BookingDto approvedOrRejectedBooking(int bookingId, int userId, boolean approved);

    BookingDto get(int bookingId, int userId);

    List<BookingDto> getAllBookingsOfUser(int userId, StateBookingEnum stateBookingEnum);

    List<BookingDto> getAllBookingsOfOwner(int userId, StateBookingEnum stateBookingEnum);
}
