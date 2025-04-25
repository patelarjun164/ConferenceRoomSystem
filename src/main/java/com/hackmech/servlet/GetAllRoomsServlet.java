package com.hackmech.servlet;

import com.google.gson.Gson;
import com.hackmech.model.Room;
import com.hackmech.service.RoomService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class GetAllRoomsServlet extends HttpServlet {
    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            List<Room> rooms = roomService.fetchAllRooms();

            for (Room room : rooms) {
                System.out.println(room.getName());
            }
            System.out.println("In servlet doGet");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            System.out.println("before GSON code");
            Gson gson = new Gson(); // or use Jackson ObjectMapper
            String json = gson.toJson(rooms);

            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
