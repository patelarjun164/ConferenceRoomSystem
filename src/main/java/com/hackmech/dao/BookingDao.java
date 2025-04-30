package com.hackmech.dao;

import com.hackmech.model.Booking;
import com.hackmech.model.Room;
import com.hackmech.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class BookingDao {

//    public void saveBooking(Booking booking) {
//        Transaction transaction = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            transaction = session.beginTransaction();
//            session.persist(booking);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        }
//    }

//    public List<Booking> getBookingsForRoom(Room room) {
//        List<Booking> bookings = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            String sql = "select * FROM booking WHERE id = ?";
//            Query<Booking> query = session.createNativeQuery(sql, Booking.class);
//            query.setParameter(1, room.getId());
//            bookings = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bookings;
//    }

    public boolean isRoomAvailable(int roomId, LocalDateTime startTime, LocalDateTime endTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println(roomId);
            String sql = "SELECT COUNT(*) FROM booking " +
                    "WHERE room_id = ? " +
                    "AND start_time < ? " +
                    "AND end_time > ?";

            NativeQuery<?> query = session.createNativeQuery(sql);
            query.setParameter(1, roomId);
            query.setParameter(2, endTime);
            query.setParameter(3, startTime);

            Number count = (Number) query.getSingleResult();
            return count.intValue() == 0; // True if no overlapping
        }
    }

    public boolean saveBooking(Booking booking) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(booking);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getBookingsByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT * FROM Booking WHERE user_id = ?";
            Query<Booking> query = session.createNativeQuery(sql, Booking.class);
            query.setParameter(1, userId);
            List<Booking> bookings = query.getResultList();
            Collections.reverse(bookings);
            return bookings;
//            for (Booking bk : bks){
//                System.out.println(bk.getId());
//            }
//            return bks;
        }
    }

    public boolean updateBooking(Booking booking) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(booking);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public Booking getBookingById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Booking.class, id);
        }
    }

    public boolean deleteBookingById(int bookingId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Booking booking = session.get(Booking.class, bookingId);
            if (booking != null) {
                session.delete(booking);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
