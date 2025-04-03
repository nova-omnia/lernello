import { error } from '@sveltejs/kit';
import { getLearningKit } from '$lib/api/learning-kit/learningKit';

export async function load({ params }: { params: { learningKitId: string } }) {
	const kit = await getLearningKit(params.learningKitId);
	if (!kit) throw error(404, 'Learning Kit not found');

	return {
		kit
	};
}
