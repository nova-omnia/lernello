import { error } from '@sveltejs/kit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { getLearningKitById } from '$lib/api/collections/learningKit';

export async function load({ params }: { params: { learningKitId: string } }) {
	const kit = await serverApiClient.req(getLearningKitById, null, params.learningKitId);
	if (!kit) throw error(404, 'Learning Kit not found');

	return {
		kit
	};
}
