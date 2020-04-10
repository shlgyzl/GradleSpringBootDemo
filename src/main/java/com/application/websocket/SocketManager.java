package com.application.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * socket管理器
 */
@Slf4j
@Component
public class SocketManager {

    // 保存socket连接
    private static ConcurrentHashMap<String, WebSocketSession> manager = new ConcurrentHashMap<>();

    // 会议参与者名称
    private static Map<Long, Set<String>> conferenceParticipants = new ConcurrentHashMap<>();

    public static void add(String key, WebSocketSession webSocketSession) {
        manager.put(key, webSocketSession);
    }

    public static void remove(String key) {
        manager.remove(key);
    }

    public static WebSocketSession get(String key) {
        return manager.get(key);
    }

    public static void join(Long conferenceId, String login) {
        conferenceParticipants
            .computeIfAbsent(conferenceId, key -> Collections.synchronizedSet(new HashSet<>()))
            .add(login);
    }

    public static void leave(Long conferenceId, String login) {
        Set<String> all = conferenceParticipants.get(conferenceId);
        if (all == null) {
            return;
        }
        all.remove(login);
        if (all.size() == 0) {
            conferenceParticipants.remove(conferenceId);
        }
    }

    public static Set<String> getParticipants(Long conferenceId) {
        return Optional.ofNullable(conferenceParticipants.get(conferenceId)).orElse(Collections.emptySet());
    }

}

