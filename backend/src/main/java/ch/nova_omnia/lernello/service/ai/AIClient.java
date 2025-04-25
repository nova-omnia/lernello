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

                The response should be formatted using Markdown and can include LaTeX syntax for mathematical expressions.
                """.formatted(topic, fullText);

        return sendRequest(prompt);
    }

    public String generateMultipleChoiceBlock(String theoryBlockContent) {
        String multipleChoicePrompt = """
                Based on the following theory content, create a multiple choice question.

                Content:
                %s

                Your response must be JSON formatted like this:
                {
                  "question": "What is ...?",
                  "possibleAnswers": ["A", "B", "C", "D"],
                  "correctAnswers": ["B"]
                }
                """.formatted(theoryBlockContent);

        return sendRequest(multipleChoicePrompt);
    }

    public String generateQuestionBlock(String theoryBlockContent) {
        String questionPrompt = """
                Based on the following theory content, create a question.

                Content:
                %s

                Your response must be JSON formatted like this:
                {
                  "question": "What is ...?",
                  "expectedAnswers": "The correct answer is ",
                }
                """.formatted(theoryBlockContent);

        return sendRequest(questionPrompt);
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
}