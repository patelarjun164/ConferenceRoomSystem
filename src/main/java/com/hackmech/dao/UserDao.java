package com.hackmech.dao;

import com.hackmech.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
