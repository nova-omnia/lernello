import { handleApiError } from '$lib/api/apiError.js';
import { getLearningUnitById } from '$lib/api/collections/learningUnit';
import { serverApiClient } from '$lib/api/serverApiClient.js';

export const load = handleApiError(async ({ params }) => {
	const { learningUnitId } = params;

	const learningUnit = await serverApiClient.req(getLearningUnitById, null, learningUnitId);

	return {
		learningUnitId,
		learningUnit
	};
});
