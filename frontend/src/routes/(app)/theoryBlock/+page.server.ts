// +page.server.ts
import { carta } from '$lib/carta';
import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async () => {
  const initialMarkdown = `# Hello from SSR
This is an example of SSR with Carta.
`;
  const renderedHtml = await carta.render(initialMarkdown);

  return {
    markdown: initialMarkdown,
    html: renderedHtml
  };
};
