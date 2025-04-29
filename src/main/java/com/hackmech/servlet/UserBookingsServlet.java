package com.hackmech.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackmech.model.Booking;
import com.hackmech.model.User;
import com.hackmech.service.BookingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/protected/user/bookings")
public class UserBookingsServlet extends HttpServlet {

    private BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId(); // Assuming your User class has a getId() method

        System.out.println(user.getName() + " from booking servlet session");
        List<Booking> bookings = bookingService.getBookingsByUser(userId);

        System.out.println("frombooking servlet ids");
//        for (Booking bk : bookings) {
//            System.out.print(bk.getId());
//        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

//        System.out.println("Before JSon");
        String json = null;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class,
                            (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                    new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .create();
            json = gson.toJson(bookings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(json);
        resp.getWriter().write(json);
    }
}
