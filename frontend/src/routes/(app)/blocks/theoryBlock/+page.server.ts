// +page.server.ts
import type { PageServerLoad, Actions } from './$types';
import { createTheoryBlock } from '$lib/api/theoryBlock';

export const load: PageServerLoad = async () => {
	const initialMarkdown = `# Hello from SSR
        This is an example of SSR with Carta.
        `;

	return { markdown: initialMarkdown };
};

export const actions: Actions = {
    create: async ({ request }) => {
        const formData = await request.formData();
        const data = {
            name: formData.get('name') as string,
            position: parseInt(formData.get('position') as string, 10),
            learningUnitId: formData.get('learningUnitId') as string,
            content: formData.get('content') as string,
        };

        try {
            await createTheoryBlock(data);
            return { success: true };
        } catch (error) {
            console.error(error);

            // Narrowing the type of 'error'
            if (error instanceof Error) {
                return { success: false, error: error.message };
            }

            // Fallback for unknown error types
            return { success: false, error: 'An unknown error occurred.' };
        }
    },
};