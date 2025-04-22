package com.hackmech.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/protected/*") // Or whatever your secure path is
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
//            res.sendRedirect(req.getContextPath() + "/login.html");
            System.out.println("redirect to login");
            res.getWriter().write("success");
        } else {
            System.out.println("valid user");
            res.getWriter().write("valid user");
            chain.doFilter(request, response);
        }
    }
}

