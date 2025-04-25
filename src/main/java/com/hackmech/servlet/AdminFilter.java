package com.hackmech.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    // Or whatever your secure path is
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("Auth Filter access by API");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String role = (String) session.getAttribute("role");
        System.out.println(role);

//        if (session == null || session.getAttribute("user") == null) {
////            res.sendRedirect(req.getContextPath() + "/login.html");
//            System.out.println("redirect to login");
//            res.getWriter().write("success");
//        }
        if (role == null || !role.equals("ADMIN")) {
//            res.sendRedirect("unauthorized.html");
            System.out.println("Unauthorised access, only admin can access");
            res.getWriter().write("unauthorised, only admin can access");
        }
        else {
            System.out.println("valid user(admin)");
            res.getWriter().write("valid user(admin)");
            chain.doFilter(request, response);
        }
    }
}

