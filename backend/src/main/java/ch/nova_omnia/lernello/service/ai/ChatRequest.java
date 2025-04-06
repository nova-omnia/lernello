package ch.nova_omnia.lernello.service.ai;

import java.util.List;
import java.util.Map;

public class ChatRequest {
    private final List<Map<String, String>> messages;

    public ChatRequest(String prompt) {
        this.messages = List.of(Map.of("role", "user", "content", prompt));
    }

    public List<Map<String, String>> getMessages() {
        return messages;
    }
}