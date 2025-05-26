package ch.nova_omnia.lernello.service.block;

public enum AIPromptTemplate {
    THEORY_BLOCK("""
            You are an AI tutor. Create a short theory block on the topic '%s', strictly based on the provided content below.

            This theory block is intended for an e-learning platform and should:
            - present the key concept in a compact and clear way,
            - use Markdown formatting (e.g. for lists, **bold**, _italic_, `code`, code blocks, headings),
            - include relevant examples, code snippets or visual elements if helpful,
            - stay within a reasonable length (max. ~100 words for the text, excluding formatting).
            - The content must be written in **English**, even if the input is in a different language.

            Content:
            %s

            IMPORTANT: Respond only with pure JSON in the following format:
            { "content": "..." }

            IMPORTANT:
            - integrate the user's personal prompt or intention: '%s',
            - adapt the tone and complexity based on the specified difficulty: '%s'
            """),

    MULTIPLE_CHOICE("""
            Based on the following content, create a multiple choice question.


            Content:
            %s

            IMPORTANT: Respond only with pure JSON:
            { "question": "...", "possibleAnswers": ["..."], "correctAnswers": ["..."] }

            - Ensure that the question is not longer than 200 characters and the possibleAnswers and correctAnswers are not longer than 100 characters each.
            - The content must be written in **English**, even if the input is in a different language.

            IMPORTANT:
            - integrate the user's personal prompt or intention: '%s',
            - adapt the tone and complexity based on the specified difficulty: '%s'
            """),

    QUESTION_BLOCK("""
            Based on the following content, create a question.

            Content:
            %s

            IMPORTANT: Respond only with pure JSON:
            { "question": "...", "expectedAnswer": "..." }

            - Ensure that both the question and the expectedAnswer are not longer than 200 characters each.
            - The content must be written in **English**, even if the input is in a different language.

            IMPORTANT:
            - integrate the user's personal prompt or intention: '%s',
            - adapt the tone and complexity based on the specified difficulty: '%s'
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
            - The content must be written in **English**, even if the input is in a different language.
            - IMPORTANT: Respond only with pure JSON
            - IMPORTANT: The topic title must be a single, standalone word, no spaces, hyphens, or special characters.
            - The format must match exactly the following structure:

            {
                "topic1_title": "topic1_text",
                "topic2_title": "topic2_text",
                ...
            }

            Content:
            %s
            """),

    TRANSLATION("""
            You are an AI translation assistant.

            Task:
            Translate the following content from its original language (which may be English or another language) into **%s**.

            Rules:
            - Always translate in the target language, even if the input appears to already be in the target language.
            - Do not skip or shorten the translation.
            - Do not include any explanation, preface, or formatting — respond with the raw translated text only.
            - Do not use JSON or quotes around the output.
            - Maintain original meaning and tone.

            Content:
            %s
            """),

    CHECK_ANSWER("""
            You are an AI tutor verifying a student's answer.

            Compare the user's answer to the expected correct answer.
            Consider the following:
            - The meaning must match, but the exact words or language may differ.
            - The user's answer may be in a different language (e.g., German, French), but if the meaning is equivalent to the expected English answer, it should be accepted as correct.
            - Allow for synonyms and alternate phrasing, but not for incorrect concepts.

            Expected answer:
            %s

            User's answer:
            %s

            IMPORTANT:
            - Respond strictly with either true or false as **plain text only** (without quotes, without explanation).
            - Respond with true only if the user's answer is semantically correct, regardless of language or phrasing.
            """);

    private final String template;

    AIPromptTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return template.formatted(args);
    }
}
