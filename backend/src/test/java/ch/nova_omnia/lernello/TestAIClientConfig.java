package ch.nova_omnia.lernello;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ch.nova_omnia.lernello.service.ai.AIClient;

@TestConfiguration
public class TestAIClientConfig {
    @Bean
    public AIClient aiClient() {
        return new AIClient() {
            @Override
            public String sendPrompt(String prompt) {
                // Return predictable dummy response for tests
                return "{}";
            }
        };
    }
}
