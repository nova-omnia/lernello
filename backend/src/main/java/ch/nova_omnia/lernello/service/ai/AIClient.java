package ch.nova_omnia.lernello.service.ai;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "http://localhost:4000/chat/completions";

    public String sendPrompt(String prompt) {
        ChatRequest request = new ChatRequest(prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ChatResponse> response = restTemplate.postForEntity(apiUrl, entity, ChatResponse.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("AI request failed: " + response.getStatusCode());
        }

        String content = Objects.requireNonNull(response.getBody()).getChoices().get(0).getMessage().getContent().trim();

        return extractJson(content);
    }

    private String extractJson(String response) {
        String trimmed = response.trim();

        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start != -1 && end != -1) {
                trimmed = trimmed.substring(start, end + 1).trim();
            }
        }

        if (trimmed.toLowerCase().startsWith("json")) {
            trimmed = trimmed.substring(4).trim();
        }

        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new RuntimeException("Invalid AI response: not pure JSON: " + trimmed);
        }

        return trimmed;
    }


}
