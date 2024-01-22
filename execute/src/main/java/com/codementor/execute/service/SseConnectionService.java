package com.codementor.execute.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseConnectionService {
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitterForUsers(String userId){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));
        return emitter;
    }

    public void sendToUser(String userId, Object data){
        SseEmitter emitter = emitters.get(userId);
        if(emitter != null){
            try{
                emitter.send(data);
            } catch (Exception e){
                emitters.remove(userId);
            }
        }
    }
}
