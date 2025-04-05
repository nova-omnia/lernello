import { deleteLearningKit, getAllLearningKits } from '$lib/api/learning-kit/learningKit';
import type { Actions } from '@sveltejs/kit';
import { fail } from '@sveltejs/kit';

export async function load() {
	const kits = await getAllLearningKits();
	return { kits };
}

export const actions: Actions = {
	delete: async ({ request }) => {
		const formData = await request.formData();
		const uuid = formData.get('uuid');

		if (!uuid || typeof uuid !== 'string') {
			return fail(400, { message: 'Missing kit ID' });
		}

		await deleteLearningKit(uuid);
		return { success: true };
	}
};
