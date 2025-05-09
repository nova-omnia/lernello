package ch.nova_omnia.lernello.service;

public enum AIPromptTemplate {
    THEORY_BLOCK("""
            You are an AI tutor. Create a short theory block on the topic '%s', strictly based on the provided content below.

            This theory block is intended for an e-learning platform and should:
            - present the key concept in a compact and clear way,
            - use Markdown formatting (e.g. for lists, **bold**, _italic_, `code`, code blocks, headings),
            - include relevant examples, code snippets or visual elements if helpful,
            - stay within a reasonable length (max. ~100 words for the text, excluding formatting).

            Content:
            %s

            IMPORTANT: Respond only with pure JSON in the following format:
            { "content": "..." }

            """),

    MULTIPLE_CHOICE("""
            Based on the following content, create a multiple choice question.

            Content:
            %s

            IMPORTANT: Respond only with pure JSON:
            { "question": "...", "possibleAnswers": ["..."], "correctAnswers": ["..."] }

            Ensure that the question is not longer than 200 characters and the possibleAnswers and correctAnswers are not longer than 100 characters each.
            """),

    QUESTION_BLOCK("""
            Based on the following content, create a question.

            Content:
            %s

            IMPORTANT: Respond only with pure JSON:
            { "question": "...", "expectedAnswer": "..." }

            Ensure that both the question and the expectedAnswer are not longer than 200 characters each.
            """),

    TOPIC_EXTRACTION("""
            You are an AI assistant. Analyze the following content and divide it into meaningful and complete topics or sections.
            Your task is to extract meaningful and pedagogically useful topics from the given content.

            Each topic must have:
            - a unique string title (as the JSON key),
            - and the corresponding full text (as the JSON value).
            - Focus only on the most important and relevant concepts suitable for a learning unit (e-learning context).
            - Do NOT split the content into too many topics. The number of topics should be reasonable and not overwhelming for learners.
            - Avoid very short or redundant topics. Group related content meaningfully.

            IMPORTANT:
            - Only include valid UTF-8 characters.
            - Avoid corrupted or non-printable characters such as 'Ã', '´┐¢', 'â', etc.
            - Replace or omit any characters that cannot be represented in clean UTF-8 text.
            - Respond with clean, readable English only.
            - IMPORTANT: Respond only with pure JSON
            - The format must match exactly the following structure:

            {
                "topic1_title": "topic1_text",
                "topic2_title": "topic2_text",
                ...
            }

            Content:
            %s
            """);

    private final String template;

    AIPromptTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return template.formatted(args);
    }
}
