package com.hackmech.servlet;

import com.hackmech.model.Room;
import com.hackmech.model.User;
import com.hackmech.service.BookingService;
import com.hackmech.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/protected/book-room")
public class BookRoomServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String purpose = request.getParameter("purpose");

        System.out.println(roomId);
        System.out.println(startTimeStr);
        System.out.println(endTimeStr);
        System.out.println(purpose);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        Room room;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            room = session.get(Room.class, roomId);
            System.out.println("from try " + room.getName());
            System.out.println("from try " + user.getName());
        }

        boolean success = bookingService.bookRoom(room, user, startTime, endTime, purpose);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body style='font-family:sans-serif; padding:2rem;'>");
        if (success) {
            System.out.println("booking confirm!");
            out.write("booking confirm!");
            out.println("<h2 style='color:green;'>✅ Booking confirmed!</h2>");
        } else {
            System.out.println("Booking failed. Room is already booked during that time.!");
            out.write("Booking failed. Room is already booked during that time.");
            out.println("<h2 style='color:red;'>❌ Booking failed. Room is already booked during that time.</h2>");
        }
        out.println("<a href='BookRoom.html'>Back to Booking</a>");
        out.println("</body></html>");
    }
}

