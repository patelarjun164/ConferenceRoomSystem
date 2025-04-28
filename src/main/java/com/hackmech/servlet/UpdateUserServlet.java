package com.hackmech.servlet;

import com.hackmech.model.User;
import com.hackmech.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/updateUser")
public class UpdateUserServlet extends HttpServlet {

    private UserService userService = new UserService(); // create object (you can also use singleton if you want)

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get parameters directly
            System.out.println("Into servlet update");
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String role = request.getParameter("role");

            System.out.println(id);
            System.out.println(name);
            System.out.println(email);
            System.out.println(role);

            // Update user
            User updatedUser = new User();
            updatedUser.setId(id);
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setRole(role);

            boolean success = userService.updateUser(updatedUser);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("User updated successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Failed to update user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Server Error");
        }
    }
}
