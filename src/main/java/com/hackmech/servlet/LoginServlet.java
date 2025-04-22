package com.hackmech.servlet;

import com.hackmech.model.User;
import com.hackmech.service.UserService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.hibernate.Session;

import java.io.IOException;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println(email);
        System.out.println(password);
//        System.out.println(password);

        if (email == null || password == null) {
            resp.getWriter().write("Missing email or password");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserService service = new UserService();
            User user = service.login(email, password, session);

            if (user != null) {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("user", user);
                httpSession.setAttribute("role", user.getRole());
                System.out.println("login success");
                resp.getWriter().write("success");
            } else {
                System.out.println("invalid username or password");
                resp.getWriter().write("invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("exception");
        }
    }
}

