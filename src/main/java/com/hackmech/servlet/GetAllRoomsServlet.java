package com.hackmech.servlet;

import com.google.gson.Gson;
import com.hackmech.model.Room;
import com.hackmech.service.RoomService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/rooms")
public class GetAllRoomsServlet extends HttpServlet {
    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Room> rooms = roomService.fetchAllRooms();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson(); // or use Jackson ObjectMapper
        String json = gson.toJson(rooms);

        response.getWriter().write(json);
    }
}
