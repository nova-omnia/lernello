import { handleApiError } from '$lib/api/apiError.js';
import { getLearningUnitById } from '$lib/api/collections/learningUnit';
import { serverApiClient } from '$lib/api/serverApiClient.js';
import type { BlockRes } from '$lib/schemas/response/BlockRes';

export const load = handleApiError(async ({ params }) => {
	const { learningUnitId } = params;

	const learningUnit = await serverApiClient.req(getLearningUnitById, null, learningUnitId);

	return {
		learningUnitId,
		blocks: [
			{ uuid: 'a1', name: 'Block 1', type: 'theory' },
			{ uuid: 'b2', name: 'Block 2', type: 'quiz' },
			{ uuid: 'c3', name: 'Block 3', type: 'theory' },
			{ uuid: 'd4', name: 'Block 4', type: 'quiz' }
		] as BlockRes[],
		learningUnit
	};
});
