package ch.nova_omnia.lernello.service.block;

public enum AIPromptTemplate {
    THEORY_BLOCK("""
            You are an AI tutor. Create a theory block on the topic '%s', strictly based on the provided content below.

            This theory block is intended for an e-learning platform and should:
            - explain the key concept in a clear, structured, and pedagogically valuable way,
            - avoid vague or overly general summaries; focus on **specific details, named concepts, technical terms, steps, or examples** from the content,
            - use Markdown formatting effectively (e.g. for lists, **bold**, _italic_, `code`, code blocks, headings),
            - include relevant examples, formulas, diagrams (described), code snippets, or analogies to enhance understanding,
            - adapt the **length to the complexity of the topic**: be concise when the concept is simple, and elaborate when the topic requires depth,
            - maintain a logical flow and structure: e.g., introduction → explanation → example → use case,
            - ensure that the explanation is **self-contained** and understandable without needing to refer to the original text,
            - be written in **clear, readable English**, even if the input content is in a different language.
            - never repeat or nest backticks inside each other (` ``` ``` ` is invalid),
            - avoid malformed Markdown such as incomplete or excessive backtick sequences.

            IMPORTANT: Use only plain ASCII and UTF-8 characters.
            - Do not use typographic punctuation like ‘ ’ “ ” or em-dashes (—).
            - Use standard ASCII alternatives: ' " - etc.
            - Omit or replace any character that cannot be safely encoded as UTF-8.

            Content:
            %s

            IMPORTANT: Respond only with pure JSON in the following format:
            { "content": "..." }

            IMPORTANT:
            - Fully integrate the user's personal learning intention or request: '%s'
            - Adapt the tone, terminology, and level of detail to match the specified difficulty: '%s'
            - If appropriate, emphasize **why** the topic matters and in what contexts it is applied
            """),

    MULTIPLE_CHOICE("""
            You are an AI tutor. Based on the following content, generate a high-quality, specific multiple choice question (MCQ) for educational use.

            Content:
            %s

            The question must:
            - be based strictly on the information in the content (no external knowledge),
            - focus on **concrete facts, definitions, steps, concepts, examples, functions, methods, distinctions, or applications**,
            - avoid vague or generic phrasing — the question must be answerable **only** by someone who has understood this specific content,
            - reflect what a learner should be able to recall, explain, or apply after studying this material.

            IMPORTANT: Respond only with pure JSON in the following format:
            {
              "question": "...",
              "possibleAnswers": ["...", "...", "...", "..."],
              "correctAnswers": ["..."]
            }

            Additional requirements:
            - The question must be no longer than 200 characters.
            - Each possible answer must be no longer than 100 characters.
            - Use **English only**, even if the input is in a different language.
            - Ensure that the **correct answer is factually correct** and that **distractors (wrong options) are plausible** but clearly incorrect if the learner knows the material.
            - Include only **one correct answer**, unless the content explicitly describes multiple valid options.

            IMPORTANT:
            - Integrate the user's specific learning intention: '%s'
            - Adapt the tone, complexity, and terminology to the requested difficulty: '%s'
            - Prefer content-relevant technical terms or key phrases in both question and answers if available
            """),

    QUESTION_BLOCK("""
            You are an AI tutor. Based on the following content, create a precise, content-specific open-ended question that tests factual or conceptual understanding.

            Content:
            %s

            The question must:
            - be **clearly answerable using only the provided content**, without relying on external knowledge,
            - focus on **specific details, definitions, concepts, steps, examples, or cause-effect relationships**,
            - be pedagogically useful – suitable for learning assessment or reflection.

            IMPORTANT: Respond only with pure JSON in the following format:
            {
              "question": "...",
              "expectedAnswer": "..."
            }

            Additional requirements:
            - Both the question and the expectedAnswer must be no longer than 200 characters each.
            - Use **English only**, even if the input is in another language.
            - The **expected answer** should be specific and directly supported by the content.
            - Do not generate overly generic questions like "What is discussed in the text?" or "Explain this topic."
            - Phrase the question so that learners **must engage with a key detail or idea** in the content.

            IMPORTANT:
            - Integrate the user's personal learning intention or prompt: '%s'
            - Adapt the tone, terminology, and cognitive depth to the specified difficulty level: '%s'
            - If appropriate, use technical terms or examples found in the content
            """),

    TOPIC_EXTRACTION("""
            You are an AI assistant. Analyze the following content and divide it into meaningful and pedagogically useful topics or sections suitable for learning.

            Your task is to extract **specific, detailed, and well-separated** topics from the given content that are suitable for e-learning or educational purposes.

            Each topic must have:
            - a unique string word (as the JSON key),
            - and the corresponding full text (as the JSON value),
            - with an emphasis on **specific**, **detail-rich** information (e.g., definitions, examples, formulas, names of methods, historical facts, key concepts, step-by-step explanations),
            - not just general summaries or chapter names.

            **JSON keys requirements (JSON keys):**
            - The key must be a real English dictionary word (not a phrase, not a compound word, not a brand or abbreviation)
            - It must contain only letters a–z.
            - VERY IMPORTANT: The key length must be between 1 and 20 characters. When generating count the characters and if the key does not follow this rule, create another one.
            - The key must still be specific and meaningful (e.g., `bios`, `boot`, `sockets`, `timers`, `virtualization`, `hashing`, `encryption`, `sorting`, etc.).
            - keys that do not follow these rules must be automatically discarded and replaced.

            Guidelines:
            - You must estimate the content length based on character or word count.
                - For content with more than 10,000 characters, extract **at least 20 topics**.
                - For content with more than 20,000 characters, extract **at least 30–40 topics**.
                - If unsure, err on the side of creating more, not fewer, as long as each topic is coherent and educationally useful.
            - If a section includes multiple named elements, processes, or facts, split them into separate topics as long as they are individually useful for learning.
            - Group only closely related ideas together. Avoid over-generalizing.
            - Avoid creating very short or redundant topics.
            - Do not over-fragment simple ideas into micro-topics, but **do break down complex content** into clear, logically separated pieces.

            Formatting & Language:
            - Include only valid UTF-8 characters.
            - Replace or omit any corrupted or non-printable characters such as 'Ã', '´┐¢', 'â', etc.
            - Respond with clean, readable English only.
            - The content must be written in **English**, even if the input is in a different language.
            - Only respond with pure JSON.

            Use this exact structure:
            {
                "key": "topic1_text",
                "key": "topic2_text",
                ...
            }

            Content:
            %s
            """),

    PROMPT_OPTIMIZER("""
            You are a prompt optimizer.

            Your task is to take the following base prompt and improve it by analyzing the specific content that will be inserted. The goal is to make the prompt more effective for generating very **detailed, pedagogically useful**, and **specific** learning topics — without changing the required JSON format or output structure.

            Instructions:
            1. Analyze the content to detect its subject area (e.g. science, history, computer science, economics, etc.).
            2. Based on the subject, enrich and sharpen the prompt instructions to guide the AI toward extracting **deep, specific, named, and example-rich** topics from the content.
            3. Add precise guidance related to the **expected detail level**, such as "include definitions, formulas, names, or examples" where appropriate.
            4. Keep the JSON output structure unchanged.
            5. DO NOT add any introductory or concluding text. Only return the improved prompt that will be used for content processing.
            6. The output must still allow the following structure exactly:

            {
                "key": "topic1_text",
                "key": "topic2_text",
                ...
            }

            Input Prompt:
            ---
            %s
            ---
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
