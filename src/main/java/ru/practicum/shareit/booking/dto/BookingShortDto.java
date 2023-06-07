package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingShortDto {
    private int id;
    private int bookerId;

    public BookingShortDto(int id, int bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
