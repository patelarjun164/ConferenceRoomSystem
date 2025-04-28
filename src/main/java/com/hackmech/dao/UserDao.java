package com.hackmech.dao;

import com.hackmech.model.User;
import com.hackmech.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao {

    public boolean registerUser(Session session, User user) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailTaken(Session session, String email) {
        try {
            return session
                    .createNativeQuery("SELECT * FROM users WHERE email = ?", User.class)
                    .setParameter(1, email)
                    .uniqueResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(Session session, String email, String password) {
        try {
            return session
                    .createNativeQuery("SELECT * FROM users WHERE email = ? and password=?", User.class)
                    .setParameter(1, email)
                    .setParameter(2, password)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<User> query = session.createNativeQuery(
                    "SELECT * FROM users", User.class);
            List<User> users = query.list();
            for (User ul : users){
                System.out.println(ul.getName());
            }
            return query.list();
        }
    }

    public List<User> searchUsersByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createNativeQuery(
                    "SELECT * FROM users WHERE LOWER(name) LIKE ?", User.class);
            query.setParameter(1, "%" + name.toLowerCase() + "%");
            return query.list();
        }
    }

    public boolean updateUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Simple SQL update based on email (assuming email is unique)
            session.createNativeQuery("UPDATE users SET name = ?, role = ? WHERE id = ?")
                    .setParameter(1, user.getName())
                    .setParameter(2, user.getRole())
                    .setParameter(3, user.getId())
                    .executeUpdate();

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
