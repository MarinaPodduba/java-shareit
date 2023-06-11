package ru.practicum.shareit.booking;

public enum StateBookingEnum {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Завершенные"),
    FUTURE("Будущие"),
    WAITING("Ожидает подтверждения"),
    REJECTED("Отклоненные");

    private final String message;

    StateBookingEnum(String message) {
        this.message = message;
    }
}
