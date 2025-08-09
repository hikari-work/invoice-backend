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

        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(response);
        }

        String tokenValue = token.substring(7); // Hapus "Bearer "

        Long expiryTime = mapData.get(tokenValue);
        if (expiryTime != null) {
            if (expiryTime > System.currentTimeMillis()) {
                return true;
            } else {
                mapData.remove(tokenValue);
            }
        }

        if (!userService.isValidToken(tokenValue)) {
            return unauthorized(response);
        }

        mapData.put(tokenValue, System.currentTimeMillis() + (1000L * 60 * 5));

        return true;
    }


    private boolean unauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Token Invalid\"}");
        response.getWriter().flush();
        return false;
    }

}
