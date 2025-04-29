package com.hackmech.servlet;

import com.hackmech.model.Room;
import com.hackmech.service.RoomService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@MultipartConfig
@WebServlet("/protected/searchRooms")
public class FindAvailableRoomsServlet extends HttpServlet {

    private RoomService roomService = new RoomService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {

// Get capacity
            int capacity = Integer.parseInt(req.getParameter("capacity"));
            System.out.println("Capacity = " + capacity);

// Get all selected equipment IDs
            String[] equipmentIdsParam = req.getParameterValues("equipment");

            List<Integer> equipmentIds = new ArrayList<>();
            if (equipmentIdsParam != null) {
                for (String idStr : equipmentIdsParam) {
                    equipmentIds.add(Integer.parseInt(idStr));
                }
            }

            Map<Room, List<Map<String, LocalDateTime>>> availableRooms =
                    roomService.findAvailableRoomsWithSlots(capacity, equipmentIds);

            JsonArray roomArray = new JsonArray();

            for (Map.Entry<Room, List<Map<String, LocalDateTime>>> entry : availableRooms.entrySet()) {
                Room room = entry.getKey();
                List<Map<String, LocalDateTime>> slots = entry.getValue();

                JsonObject roomJson = new JsonObject();
                roomJson.addProperty("id", room.getId());
                roomJson.addProperty("name", room.getName());
                roomJson.addProperty("location", room.getLocation());
                roomJson.addProperty("capacity", room.getCapacity());

                JsonArray slotArray = new JsonArray();
                for (Map<String, LocalDateTime> slot : slots) {
                    JsonObject slotJson = new JsonObject();
                    slotJson.addProperty("start", slot.get("start").toString());
                    slotJson.addProperty("end", slot.get("end").toString());
                    slotArray.add(slotJson);
                }

                roomJson.add("occupiedSlots", slotArray);

                roomArray.add(roomJson);
            }

            resp.getWriter().write(roomArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid request\"}");
        }
    }
}
