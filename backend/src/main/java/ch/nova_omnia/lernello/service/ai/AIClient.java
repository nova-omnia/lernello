package ch.nova_omnia.lernello.service.ai;

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

    public String generateBlockContent(String fullText, String topic, String blockType) {
        String prompt = """
            Du bist ein KI-Tutor. Erstelle einen %s-Block zum Thema '%s'.
            Inhalt des Kapitels:
            %s
            """.formatted(blockType, topic, fullText);

        ChatRequest request = new ChatRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatResponse> response = restTemplate.postForEntity(apiUrl, entity, ChatResponse.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("AI response failed");
        }

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}