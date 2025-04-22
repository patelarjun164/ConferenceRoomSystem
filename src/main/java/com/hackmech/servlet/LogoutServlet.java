package com.hackmech.servlet;

import com.hackmech.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                System.out.println("logout success");
                System.out.println(user.getName());
                System.out.println(user.getEmail());
                response.getWriter().write("success");
                session.invalidate();
            } else {
                response.getWriter().write("user not loggedIn yet!");
            }
            response.setStatus(HttpServletResponse.SC_OK);
//            response.sendRedirect("login.html");
        } catch (Exception e) {
            e.printStackTrace(); // Log to console (or use a logging framework)
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Logout failed.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

