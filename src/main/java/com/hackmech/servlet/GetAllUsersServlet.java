package com.hackmech.servlet;

import com.google.gson.GsonBuilder;
import com.hackmech.model.User;
import com.hackmech.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/admin/getAllUsers")
public class GetAllUsersServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return;
        }

        String search = req.getParameter("search");
        List<User> users;

        if (search != null && !search.isEmpty()) {
            users = userService.searchUsersByName(search);
        } else {
            users = userService.getAllUsers();
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .create();
        out.print(gson.toJson(users));
        System.out.println(gson.toJson(users));
        out.flush();
    }
}
