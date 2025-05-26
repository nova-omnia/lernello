package ch.nova_omnia.lernello.service.aiClient;

import java.util.List;
import java.util.Map;

public class ChatRequest {
    private final List<Map<String, String>> messages;
    private final String model;

    // Balanced creativity
    private final double temperature = 0.5;

    // Reduce repetition
    private final double frequency_penalty = 0.6;

    public ChatRequest(String prompt) {
        this.model = "gpt-4o-mini";
        this.messages = List.of(Map.of("role", "user", "content", prompt));
    }

    public List<Map<String, String>> getMessages() {
        return messages;
    }

    public String getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFrequency_penalty() {
        return frequency_penalty;
    }
}