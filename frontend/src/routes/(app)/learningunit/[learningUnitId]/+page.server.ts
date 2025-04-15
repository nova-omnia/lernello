import { api } from '$lib/api/apiClient.js';
import { handleApiError } from '$lib/api/apiError.js';
import { getLearningUnitById } from '$lib/api/collections/learningUnit';

export const load = handleApiError(async ({ params, fetch }) => {
	const { learningUnitId } = params;

	const learningUnit = await api(fetch).req(getLearningUnitById, null, learningUnitId).parse();

	return {
		learningUnitId,
		learningUnit
	};
});
