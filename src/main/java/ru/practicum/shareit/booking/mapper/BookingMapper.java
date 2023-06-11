package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.StatusBookingEnum;
import ru.practicum.shareit.booking.dto.AddNewBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static Booking mapToAddNewBooking(AddNewBookingDto addNewBookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(addNewBookingDto.getStart());
        booking.setEnd(addNewBookingDto.getEnd());
        booking.setStatus(StatusBookingEnum.WAITING);
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());

        BookingDto.Booker user = new BookingDto.Booker();
        user.setId(booking.getBooker().getId());
        user.setName(booking.getBooker().getName());
        bookingDto.setBooker(user);

        BookingDto.ItemBooking item = new BookingDto.ItemBooking();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        bookingDto.setItem(item);
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart()
        );
    }
}