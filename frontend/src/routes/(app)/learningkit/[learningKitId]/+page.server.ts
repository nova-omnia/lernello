import { error } from '@sveltejs/kit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { getLearningKitById } from '$lib/api/collections/learningKit';
import type { LearningKitRes } from '$lib/schemas/response/LearningKitRes';
import { LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';

export async function load({ params }: { params: { learningKitId: string } }) {
	const kit = await serverApiClient.req(getLearningKitById, null, params.learningKitId);
	if (!kit) throw error(404, 'Learning Kit not found');
	const kitToEdit = LearningKitResSchema.parse(kit);

	return {
		kitToEdit
	};
}
