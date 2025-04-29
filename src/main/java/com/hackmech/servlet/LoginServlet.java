package com.hackmech.servlet;

import com.hackmech.model.User;
import com.hackmech.service.UserService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

import java.io.IOException;

@MultipartConfig
@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("Into login servlet");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println(email);
        System.out.println(password);

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
                System.out.println("Over Success Message");
                resp.getWriter().write("success");
//                resp.sendRedirect("/DashBoard.html");
                resp.setStatus(HttpServletResponse.SC_OK);
                System.out.println("Below success Message");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("exception");
        }
    }
}

