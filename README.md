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

### OpenAI API Key (Required for AI features)

To use AI functionality, set the `OPENAI_API_KEY` **as an environment variable** before starting the backend.

#### Temporarily in your terminal:

- PowerShell (Windows):
  $env:OPENAI_API_KEY = "sk-..."

- CMD (Windows):
  set OPENAI_API_KEY=sk-...

- Bash/macOS/Linux:
  export OPENAI_API_KEY=sk-...

#### Or permanently in IntelliJ:

1. Go to `Run > Edit Configurations`
2. Select your Spring Boot configuration
3. Add under Environment variables:
   OPENAI_API_KEY=sk-...

#### Optional: In `application.properties` (not recommended for production)

OPENAI_API_KEY=sk-...

## Frontend (`/frontend`)

SvelteKit, TypeScript, Skeleton, TailwindCSS, Prettier, Eslint.

### Requirements

- NodeJS

### Scripts

- `npm i` to install dependencies
- `npm run dev` to start a local development server
- `npm run lint` to lint check
- `npm run format` to format with prettier (your IDE can do this automatically)

## Swagger

If you want to see what API endpoints are available...

- http://localhost:8080/swagger-ui/index.html

## H2 Database

- To start and connect the DB run the `gradle bootRun` script.
- To test and show the DB, go to the console
  - http://localhost:8080/h2-console
  - Connect with the url: `jdbc:h2:./data/LernelloData`
