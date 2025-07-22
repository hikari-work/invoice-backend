package org.yann.integerasiorderkuota.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.bot.entity.CustomerState;
import org.yann.integerasiorderkuota.bot.entity.UserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();



    public UserSession getSession(String userId) {
        return sessions.get(userId);
    }

    public UserSession getOrCreateSession(String userId, String messageId) {
        return sessions.computeIfAbsent(userId, id ->
                UserSession.builder()
                        .customerState(CustomerState.IDLE)
                        .messageId(messageId)
                        .userId(userId)
                        .build()
        );
    }


    public void updateSession(String userId, UnaryOperator<UserSession> updater) {
        sessions.computeIfPresent(userId, (id, session) -> updater.apply(session));
    }

    public void removeSession(String userId) {
        sessions.remove(userId);
    }
}
