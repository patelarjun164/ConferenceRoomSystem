package com.hackmech.dao;
import com.hackmech.model.Room;

import com.hackmech.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.*;

public class RoomDao {

    public boolean saveRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(room);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRoom(int roomId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Room room = session.get(Room.class, roomId);
            if (room != null) {
                transaction = session.beginTransaction();
                session.remove(room);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
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
//            System.out.println("into Room DAo getAllrooms sql");
//            for (Room room : rooms) {
//                System.out.println(room.getName());
//            }

            session.close();
            Collections.reverse(rooms);
            return rooms;
        }
    }


    public Map<Room, List<Map<String, LocalDateTime>>> findAvailableRoomsWithOccupiedSlots(int capacity, List<Integer> equipmentIds) {
        Map<Room, List<Map<String, LocalDateTime>>> result = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Step 1: Find matching rooms
            String sql = "SELECT DISTINCT r.* FROM rooms r " +
                    "JOIN room_equipment re ON r.id = re.room_id " +
                    "WHERE r.capacity >= :capacity " +
                    "AND re.equipment_id IN (:equipmentIds)";

            Query query = session.createNativeQuery(sql, Room.class);
            query.setParameter("capacity", capacity);
            query.setParameterList("equipmentIds", equipmentIds);


            List<Room> rooms = query.list();

            // Step 2: For each room, find occupied slots
            for (Room room : rooms) {
                String bookingSql = "SELECT start_time, end_time FROM booking WHERE room_id = ?";
                NativeQuery<Object[]> bookingQuery = session.createNativeQuery(bookingSql);
                bookingQuery.setParameter(1, room.getId());

                List<Object[]> bookings = bookingQuery.list();
                List<Map<String, LocalDateTime>> occupiedSlots = new ArrayList<>();

                for (Object[] booking : bookings) {
                    Map<String, LocalDateTime> slot = new HashMap<>();
                    slot.put("start", ((java.sql.Timestamp) booking[0]).toLocalDateTime());
                    slot.put("end", ((java.sql.Timestamp) booking[1]).toLocalDateTime());
                    occupiedSlots.add(slot);
                }
                Collections.reverse(occupiedSlots);

                result.put(room, occupiedSlots);
            }
        }
        return result;
    }
}