package com.hackmech.servlet;

import com.hackmech.model.User;
import com.hackmech.service.UserService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.hibernate.Session;

import java.io.IOException;

@MultipartConfig
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

        User user = new User(name, email, password, "EMPLOYEE");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserService service = new UserService();
            boolean success = service.register(user, session);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.getWriter().write("success");
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict (e.g., duplicate email)
                resp.getWriter().write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write("exception");
        }
    }
}
