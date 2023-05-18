package ru.practicum.shareit.booking;

public enum StatusBookingEnum {
    WAITING, // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    REJECTED, // бронирование отклонено владельцем
    CANCELED // бронирование отменено создателем
}
