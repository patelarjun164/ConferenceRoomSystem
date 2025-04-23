package com.hackmech.dao;

import com.hackmech.model.Equipment;
import com.hackmech.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EquipmentDao {
    public List<Equipment> getAllEquipment() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery("SELECT * from Equipment", Equipment.class).list();
        }
    }

    public Equipment getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Equipment.class, id);
        }
    }

    public Equipment getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT * FROM rooms";
            return session
                    .createNativeQuery("SELECT * from Equipment where name = ?", Equipment.class)
                    .setParameter("name", name)
                    .uniqueResult();
        }
    }
}
