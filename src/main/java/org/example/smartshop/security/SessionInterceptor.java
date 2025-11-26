package org.example.smartshop.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.smartshop.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class SessionInterceptor implements HandlerInterceptor {
    private final Set<String> adminPaths = Set.of("/api/admin");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.startsWith("/api/auth") || path.startsWith("/api/public")) {
            return true;
        }

        var session = request.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized, please login");
            return false;
        }

        String roleName = (String) session.getAttribute("USER_ROLE");
        if (roleName == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden, role missing");
            return false;
        }

        UserRole role = UserRole.valueOf(roleName);
        if (path.startsWith("/api/admin") && role != UserRole.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden, requires ADMIN");
            return false;
        }
        if (path.startsWith("/api/client") && role != UserRole.CLIENT) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden, requires CLIENT");
            return false;
        }

        return true;
    }
}
