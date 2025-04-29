package com.hackmech.servlet;

import com.hackmech.model.Room;
import com.hackmech.model.User;
import com.hackmech.service.BookingService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/protected/book-room")
@MultipartConfig
public class BookRoomServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || httpSession.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"User not logged in\"}");
            return;
        }

        User user = (User) httpSession.getAttribute("user");

        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String purpose = request.getParameter("purpose");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

            Room room;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                room = session.get(Room.class, roomId);
            }

            boolean success = bookingService.bookRoom(room, user, startTime, endTime, purpose);

            response.setContentType("application/json");
            if (success) {
                response.getWriter().write("{\"status\":\"success\", \"message\":\"Booking confirmed\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Room already booked during this time\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid input or internal error\"}");
        }
    }
}
