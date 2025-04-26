package com.hackmech.servlet;

import com.hackmech.model.User;
import com.google.gson.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/session")
public class FetchSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"message\": \"No active session\"}");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("id", user.getId());
        jsonResponse.addProperty("name", user.getName());
        jsonResponse.addProperty("email", user.getEmail());
        jsonResponse.addProperty("role", role);

        resp.getWriter().write(jsonResponse.toString());
    }
}
