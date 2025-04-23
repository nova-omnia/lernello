package ch.nova_omnia.lernello.service.ai;

import java.util.List;
import java.util.Map;

public class ChatRequest {
    private final List<Map<String, String>> messages;
    private final String model;

    public ChatRequest(String prompt) {
        this.model = "gpt-3.5-turbo";
        this.messages = List.of(Map.of("role", "user", "content", prompt));
    }

    public List<Map<String, String>> getMessages() {
        return messages;
    }

    public String getModel() {
        return model;
    }
}