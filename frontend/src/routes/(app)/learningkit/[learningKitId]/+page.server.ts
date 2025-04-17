import { getLearningKitById } from '$lib/api/collections/learningKit';
import { handleApiError } from '$lib/api/apiError';
import { api } from '$lib/api/apiClient.js';

export const load = handleApiError(async ({ params, fetch }) => {
	const kit = await api(fetch).req(getLearningKitById, null, params.learningKitId).parse();

	return {
		kitToDisplay: kit
	};
});
