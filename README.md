# PM4

Hi 👉👈

## Backend (`/backend`)

Java, Gradle, SpringBoot.

### Requirements

- Java 23
- Gradle 8+ (or just use gradlew)

### Scripts

- `gradle bootRun` to start the server (will auto restart on rebuild)
- `gradle build --continuous` to auto build when files change

## Frontend (`/frontend`)

SvelteKit, TypeScript, Skeleton, TailwindCSS, Prettier, Eslint.

### Requirements

- NodeJS

### Scripts

- `npm i` to install dependencies
- `npm run dev` to start a local development server
- `npm run lint` to lint check
- `npm run format` to format with prettier (your IDE can do this automatically)

## LiteLLM (`/litellm`)

LiteLLM: so we can easily switch between different AI models during development.

### Requirements

- Docker
- Ollama with deepseek-r1 (make sure ollama is running if you want to use that model)

### Scripts

- `docker compose up` (for older versions: `docker-compose up`) to start
