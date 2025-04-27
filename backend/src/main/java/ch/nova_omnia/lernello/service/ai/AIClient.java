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

    public String generateTheoryBlock(String fullText, String topic) {
        String prompt = """
                You are an AI tutor. Create a theory block on the topic '%s'.
                Content of the chapter:
                %s

                Response must be a JSON object only without markdown formatting, code blocks, or extra text.
                Example:
                {
                  "content": "Your theory content here"
                }

                Use Markdown and LaTeX syntax inside the content string where appropriate.
                """.formatted(topic, fullText);

        return extractJson(sendRequest(prompt));
    }

    public String generateMultipleChoiceBlock(String theoryBlockContent) {
        String prompt = """
                Based on the following theory content, create a multiple choice question.

                Content:
                %s

                Response must be a JSON object only:
                {
                  "question": "What is ...?",
                  "possibleAnswers": ["A", "B", "C", "D"],
                  "correctAnswers": ["B"]
                }
                No markdown, no code block, just pure JSON.
                """.formatted(theoryBlockContent);

        return extractJson(sendRequest(prompt));
    }

    public String generateQuestionBlock(String theoryBlockContent) {
        String prompt = """
                Based on the following theory content, create a question.

                Content:
                %s

                Response must be a JSON object only:
                {
                  "question": "What is ...?",
                  "expectedAnswer": "The correct answer is ..."
                }
                No markdown, no code block, just pure JSON.
                """.formatted(theoryBlockContent);

        return extractJson(sendRequest(prompt));
    }

    private String sendRequest(String prompt) {
        ChatRequest request = new ChatRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatResponse> response = restTemplate.postForEntity(apiUrl, entity, ChatResponse.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("AI response failed: " + response.getStatusCode());
        }

        ChatResponse responseBody = Objects.requireNonNull(response.getBody(), "Response body is null");
        if (responseBody.getChoices() == null || responseBody.getChoices().isEmpty()) {
            throw new RuntimeException("AI response contains no choices");
        }

        return responseBody.getChoices().get(0).getMessage().getContent();
    }

    private String extractJson(String response) {
        String trimmed = response.trim();
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            trimmed = trimmed.substring(3, trimmed.length() - 3).trim();
        }
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new RuntimeException("AI response is not pure JSON: " + trimmed);
        }
        return trimmed;
    }
}
