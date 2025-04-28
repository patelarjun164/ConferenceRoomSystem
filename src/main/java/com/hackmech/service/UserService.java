package com.hackmech.service;

import com.hackmech.dao.UserDao;
import com.hackmech.model.User;
import org.hibernate.Session;

import java.util.List;

public class UserService {

    private UserDao dao = new UserDao();

    public boolean register(User user, Session session) {
        if (user.getEmail() == null || user.getPassword().length() < 6) return false;
        if (dao.isEmailTaken(session, user.getEmail())) return false;

        return dao.registerUser(session, user);
    }

    public User login(String email, String password, Session session) {
        if (email == null || password == null) return null;
        return dao.loginUser(session, email, password);
    }

    public List<User> getAllUsers() {
        List<User> list =  dao.getAllUsers();
        for (User ul : list){
            System.out.println("in service " + ul.getName());
        }
//        return dao.getAllUsers();
        return list;
    }

    public List<User> searchUsersByName(String name) {
        return dao.searchUsersByName(name);
    }

    public boolean updateUser(User user) {
        return dao.updateUser(user);
    }
}
