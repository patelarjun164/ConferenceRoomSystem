package com.hackmech.service;

import com.hackmech.dao.BookingDao;
import com.hackmech.model.Booking;
import com.hackmech.model.Room;
import com.hackmech.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingDao bookingDAO = new BookingDao();

    public boolean isRoomAvailable(Room room, LocalDateTime start, LocalDateTime end) {
        List<Booking> existingBookings = bookingDAO.getBookingsForRoom(room);

        System.out.println("---------");
        System.out.println("room id " + room.getId());

        for (Booking booking : existingBookings) {
            if (start.isBefore(booking.getEndTime()) && end.isAfter(booking.getStartTime())) {
                // There is a time overlap
                System.out.println("OVERLap checking started__-----");
                System.out.println("new start time "  + start);
                System.out.println("old start time" + booking.getStartTime());
                System.out.println("new end time "  + end);
                System.out.println("old end time" + booking.getEndTime());
                System.out.println("OVERLap checking ended__-----");
                return false;
            }
        }

        return true;
    }

    public boolean bookRoom(Room room, User user, LocalDateTime start, LocalDateTime end, String purpose) {
        if (!isRoomAvailable(room, start, end)) {
            return false; // Conflict overlap timing
        }

        Booking newBooking = new Booking(room, user, start, end, purpose);
        bookingDAO.saveBooking(newBooking);
        return true;
    }

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> bks = bookingDAO.getBookingsByUserId(userId);
        for (Booking bk : bks){
            System.out.println(bk.getId());
        }
        return bks;
    }
}

