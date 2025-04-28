package com.hackmech.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*") // Filter all admin-related URLs
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("Admin Filter access by API");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No session available. Unauthorized access.");
            return;
        }

        String role = (String) session.getAttribute("role");
        System.out.println("User role: " + role);

        // Check if the user is an admin
        if (role == null || !role.equals("ADMIN")) {
            System.out.println("Unauthorized access, only admin can access.");
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized, only admins can access this resource.");
        } else {
            // If the user is an admin, proceed with the filter chain
            System.out.println("Valid admin user");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization (if needed)
    }

    @Override
    public void destroy() {
        // Cleanup (if needed)
    }
}
