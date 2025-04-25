package com.hackmech.dao;

import com.hackmech.model.Booking;
import com.hackmech.model.Room;
import com.hackmech.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BookingDao {

    public void saveBooking(Booking booking) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Booking> getBookingsForRoom(Room room) {
        List<Booking> bookings = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "select * FROM booking WHERE id = ?";
            Query<Booking> query = session.createNativeQuery(sql, Booking.class);
            query.setParameter(1, room.getId());
            bookings = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT * FROM Booking WHERE user_id = ?";
            Query<Booking> query = session.createNativeQuery(sql, Booking.class);
            query.setParameter(1, userId);
            List<Booking> bks = query.getResultList();
            for (Booking bk : bks){
                System.out.println(bk.getId());
            }
            return bks;
        }
    }
}
