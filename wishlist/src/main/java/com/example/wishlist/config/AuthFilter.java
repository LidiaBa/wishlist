package com.example.wishlist.config;

import com.example.wishlist.domain.User;
import com.example.wishlist.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals("/users") && request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            sendError(response, 401, "Authorization error");
            return;
        }

        String token = authHeader;
        if (authHeader.contains("Bearer")) {
            token = authHeader.substring(6).trim();
        }

        User user = authService.auth(token);
        if (user == null) {
            sendError(response, 401, "User not found");
            return;
        }

        request.setAttribute("userRole", user.getRoles());
        request.setAttribute("userId", user.getId());
        request.setAttribute("userid", user);

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print("{\"error\":\"" + message + "\"}");
    }
}
