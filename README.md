# PM4

Hi 👉👈

If you're interested in our project-sketch, you can find it under [Lernello_krea_Projektskizze_PM4_FS25.pdf](documentation/Lernello_krea_Projektskizze_PM4_FS25.pdf).

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

## Swagger

If you want to see what API endpoints are available...

- http://localhost:8080/swagger-ui/index.html

## H2 Database

- To start and connect the DB run the `gradle bootRun` script.
- To test and show the DB, go to the console
  - http://localhost:8080/h2-console
  - Connect with the url: `jdbc:h2:./data/LernelloData`
