package com.hackmech.servlet;

import com.hackmech.model.User;
import com.hackmech.service.UserService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.hibernate.Session;

import java.io.IOException;

@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        System.out.println(name);
        System.out.println(email);
        System.out.println(password);
        System.out.println(role);

        User user = new User(name, email, password, role);
//        user.setName(name);
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setRole("EMPLOYEE");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserService service = new UserService();
            boolean success = service.register(user, session);

            if (success) {
                //if success, redirect
                resp.getWriter().write("success");
            } else {
                //else show error
                resp.getWriter().write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("exception");
        }
    }
}
