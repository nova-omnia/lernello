import type { Actions } from '@sveltejs/kit';
import { fail } from '@sveltejs/kit';
import { serverApiClient } from "$lib/api/serverApiClient";
import { deleteLearningKit, getAllLearningKits } from "$lib/api/collections/learningKit";

export async function load() {
	const kits = await serverApiClient.req(getAllLearningKits, null);
	return { kits };
}

export const actions: Actions = {
	delete: async ({ request }) => {
		const formData = await request.formData();
		const uuid = formData.get('uuid');

		if (!uuid || typeof uuid !== 'string') {
			return fail(400, { message: 'Missing kit ID' });
		}

		await serverApiClient.req(deleteLearningKit, null, uuid);
		return { success: true };
	}
};
