package com.hackmech.servlet;

import com.hackmech.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/protected/delete/book")
public class DeleteBookingServlet extends HttpServlet {

    private BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookingIdStr = req.getParameter("bookingId");

        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Booking ID is missing.");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);

            boolean deleted = bookingService.deleteBooking(bookingId);

            resp.setContentType("text/plain");
            if (deleted) {
                resp.getWriter().write("Booking deleted successfully.");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Booking not found or already deleted.");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid Booking ID.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Failed to delete booking.");
        }
    }
}