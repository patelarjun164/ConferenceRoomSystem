package com.hackmech.dao;
import com.hackmech.model.Room;

import com.hackmech.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class RoomDao {

    public void saveRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(room);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoom(int roomId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Room room = session.get(Room.class, roomId);
            if (room != null) {
                transaction = session.beginTransaction();
                session.remove(room);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Room getRoomById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Room.class, id);
        }
    }

    public List<Room> getAllRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT * FROM rooms";
//            rsession.createCriteria(Room.class).list();
//            return session.createNativeQuery(sql, Room.class).getResultList();
            List<Room> rooms = session.createNativeQuery(sql, Room.class).list();

            // Manually initialize the lazy collection
//            for (Room room : rooms) {
//                Hibernate.initialize(room.getEquipment());
//            }
            System.out.println("into Room DAo getAllrooms sql");
            for (Room room : rooms) {
                System.out.println(room.getName());
            }

            session.close();
            return rooms;
        }
    }
}