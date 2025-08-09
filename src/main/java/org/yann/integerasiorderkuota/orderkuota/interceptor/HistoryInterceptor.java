package org.yann.integerasiorderkuota.orderkuota.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HistoryInterceptor implements HandlerInterceptor {

    private final UserService userService;

    private final Map<String, Long> mapData = new ConcurrentHashMap<>();

    public HistoryInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || mapData.containsKey(token.substring(7))) {
            if (mapData.get(token) > System.currentTimeMillis()) {
                return true;
            } else {
                mapData.remove(token);
                if (token == null || !token.startsWith("Bearer ") || !userService.isValidToken(token.substring(7))) {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Token Invalid\"}");
                    response.getWriter().flush();
                    return false;
                }
                mapData.put(token.substring(7), System.currentTimeMillis() + 1000 * 60 * 5); // 5 minutes
            }
        }

        return true;
    }
}
