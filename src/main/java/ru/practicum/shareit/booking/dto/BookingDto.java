package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.StatusBookingEnum;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private int id;
    private int itemId;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemBooking item;
    private Booker booker;
    private StatusBookingEnum status;

    @Data
    public static class ItemBooking {
        private int id;
        private String name;
    }

    @Data
    public static class Booker {
        private int id;
        private String name;
    }
}
