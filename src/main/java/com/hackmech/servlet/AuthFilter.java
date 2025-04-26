package com.hackmech.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/protected/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("User not found, sending unauthorized response.");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Send 401 status
            res.getWriter().write("unauthorized"); // Send text response
        } else {
            System.out.println("Valid user, continuing request.");
            chain.doFilter(request, response);
        }
    }
}
