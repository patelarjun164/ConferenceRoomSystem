package com.hackmech.servlet;

import com.hackmech.model.Booking;
import com.hackmech.service.BookingService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/protected/update/book")
public class UpdateBookingServlet extends HttpServlet {

    private BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            System.out.println("access get in update booking");

            String bookingIdStr = req.getParameter("bookingId");
            System.out.println("bookingIdStr: " + bookingIdStr);

            int bookingId = Integer.parseInt(bookingIdStr);

            String purpose = req.getParameter("purpose");
            String startTimeStr = req.getParameter("startTime");
            String endTimeStr = req.getParameter("endTime");

            Booking booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Booking ID not found!");
                return;
            }


            booking.setPurpose(purpose);
            booking.setStartTime(LocalDateTime.parse(startTimeStr));
            booking.setEndTime(LocalDateTime.parse(endTimeStr));

            boolean success = bookingService.updateBooking(booking,LocalDateTime.parse(startTimeStr), LocalDateTime.parse(endTimeStr));

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            if (success) {
                resp.getWriter().write("Booking updated successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                resp.getWriter().write("Failed to update booking.");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid booking ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Unexpected error occurred.");
        }
    }
}
