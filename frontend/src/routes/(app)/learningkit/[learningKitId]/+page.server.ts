import { error } from '@sveltejs/kit';
import { getLearningKitById } from '$lib/api/collections/learningKit';
import { LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';
import { api } from '$lib/api/apiClient.js';

export async function load({ params, fetch }) {
	const kit = await api(fetch).req(getLearningKitById, null, params.learningKitId).parse();

	if (!kit) throw error(404, 'Learning Kit not found');
	const kitToDisplay = LearningKitResSchema.parse(kit);

	return {
		kitToDisplay
	};
}
