package com.hackmech.service;

import com.hackmech.dao.BookingDao;
import com.hackmech.model.Booking;
import com.hackmech.model.Room;
import com.hackmech.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingDao bookingDAO = new BookingDao();

    public boolean bookRoom(Room room, User user, LocalDateTime startTime, LocalDateTime endTime, String purpose) {
        // Check for overlapping bookings
        boolean isAvailable = bookingDAO.isRoomAvailable(room.getId(), startTime, endTime);
        if (!isAvailable) return false;

        // If available, create and save booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setPurpose(purpose);

        return bookingDAO.saveBooking(booking);
    }

    public List<Booking> getBookingsByUser(int userId) {
//        List<Booking> bks = bookingDAO.getBookingsByUserId(userId);
//        for (Booking bk : bks){
//            System.out.println(bk.getId());
//        }
        return bookingDAO.getBookingsByUserId(userId);
    }

    public boolean updateBooking(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        boolean isAvailable = bookingDAO.isRoomAvailable(booking.getRoom().getId(), startTime, endTime);
        if (!isAvailable) return false;
        return bookingDAO.updateBooking(booking);
    }

    public Booking getBookingById(int id) {
        return bookingDAO.getBookingById(id);
    }

    public boolean deleteBooking(int bookingId) {
        return bookingDAO.deleteBookingById(bookingId);
    }
}

