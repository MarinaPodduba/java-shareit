package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingShortDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;

    public BookingShortDto(int id, int bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
