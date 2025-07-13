package org.yann.integerasiorderkuota.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yann.integerasiorderkuota.service.UserService;

@Component
public class HistoryInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public HistoryInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ") || !userService.isValidToken(token.substring(7))) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token Invalid\"}");
            response.getWriter().flush();
            return false;
        }
        return true;
    }
}
