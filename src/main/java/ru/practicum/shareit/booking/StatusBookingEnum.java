package ru.practicum.shareit.booking;

public enum StatusBookingEnum {
    WAITING("Новое бронирование, ожидает одобрения"),
    APPROVED("Бронирование подтверждено владельцем"),
    REJECTED("Бронирование отклонено владельцем"),
    CANCELED("Бронирование отменено создателем");

    private final String messageStatus;

    private StatusBookingEnum(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
