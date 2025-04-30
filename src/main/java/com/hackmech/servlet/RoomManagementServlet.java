package com.hackmech.servlet;

import com.hackmech.model.Room;
import com.hackmech.service.RoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

@MultipartConfig
@WebServlet("/admin/room")
public class RoomManagementServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        try {
            String name = request.getParameter("name");
            String location = request.getParameter("location");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String[] equipmentIds = request.getParameterValues("equipment");

            System.out.println(name);
            System.out.println(location);
            System.out.println(capacity);

            Room room = new Room();
            room.setName(name);
            room.setLocation(location);
            room.setCapacity(capacity);

            boolean isSaved = roomService.saveOrUpdateRoom(room, equipmentIds);

            if (isSaved) {
                System.out.println("Room saved/updated successfully.");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("success");
            } else {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
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

        // Check if the session is null or if the user is not an admin
        if (session == null) {
            // If the user is not logged in, redirect to login page
            response.sendRedirect("Login.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            // If the user is not an admin, respond with a 403 Forbidden error
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Unauthorized access. Only Admins can delete rooms.");
            return;
        }

        // Try deleting the room if the user is an admin
        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            roomService.deleteRoom(roomId);

            // Send success response
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            // If there is an error during the deletion, send an internal server error
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
        }
    }


}
