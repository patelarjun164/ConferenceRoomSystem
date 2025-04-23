package com.hackmech.servlet;

import com.hackmech.model.Room;
import com.hackmech.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/room")
public class RoomManagementServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            System.out.println("User is NOT ADMIN");
            response.getWriter().write("not admin!!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String[] equipmentIds = request.getParameterValues("equipment");

            Room room;
            String roomIdStr = request.getParameter("roomId");

            if (roomIdStr != null && !roomIdStr.isEmpty()) {
                int roomId = Integer.parseInt(roomIdStr);
                room = roomService.getRoomById(roomId);
                if (room == null) {
                    System.out.println("room not found!");
                    response.getWriter().write("room not found!");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            } else {
                room = new Room();
            }

            room.setName(name);
            room.setLocation(location);
            room.setCapacity(capacity);

            roomService.saveOrUpdateRoom(room, equipmentIds);

            System.out.println("Room saved/updated successfully.");
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error occurred: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            System.out.println(roomId);
            roomService.deleteRoom(roomId);

            System.out.println("Room deleted!");
            response.getWriter().write("Room deleted!");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error occurred: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
