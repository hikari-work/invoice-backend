package org.yann.integerasiorderkuota.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.bot.entity.UserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

    public Map<String, UserSession> getSession(String userId) {
        return sessions;
    }

    public void saveSession(UserSession session) {
        sessions.put(session.getUserId(), session);
    }
    public void removeSession(String userId) {
        sessions.remove(userId);
    }
}
