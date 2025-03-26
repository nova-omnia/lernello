// +page.server.ts
import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async () => {
	const initialMarkdown = `# Hello from SSR
        This is an example of SSR with Carta.
        `;

	return { markdown: initialMarkdown };
};
