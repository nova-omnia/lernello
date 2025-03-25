// src/routes/(app)/theoryBlock/+page.server.ts
import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async () => {
  const mockText = `
# Willkommen im TheoryBlock 🧠

Hier kannst du **Markdown** und sogar $$\\LaTeX$$ schreiben!

Beispiel:
\`\`\`js
console.log("Hallo Welt");
\`\`\`

Und Mathe:
$$E = mc^2$$
  `

  return {
    content: mockText
  };
};
