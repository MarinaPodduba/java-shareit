package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.StatusBookingEnum;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerId(int bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(int bookerId, LocalDateTime start,
                                                                 LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(int bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(int bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, StatusBookingEnum status, Sort sort);

    List<Booking> findAllByItemOwnerId(int bookerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(int bookerId,
                                                                    LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(int bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(int bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(int bookerId, StatusBookingEnum status, Sort sort);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(int userId, int itemId,
                                                                  StatusBookingEnum status, LocalDateTime end);

    @Query("select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id) " +
            "from Booking as b " +
            "where b.item.id=?1 and b.item.owner.id=?2 and b.status=?3 and b.start>?4 " +
            "order by b.start asc")
    List<BookingShortDto> getNextBooking(int itemId, int userId, StatusBookingEnum status,
                                         LocalDateTime now, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingShortDto(b.id, b.booker.id) " +
            "from Booking as b " +
            "where b.item.id=?1 and b.item.owner.id=?2 and b.status=?3 and b.start<?4 " +
            "order by b.start desc")
    List<BookingShortDto> getLastBooking(int itemId, int userId, StatusBookingEnum status,
                                                LocalDateTime now, Pageable pageable);

    List<Booking> findByItemIdIn(Set<Integer> itemId, Sort sort);
}
