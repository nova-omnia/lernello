import { serverApiClient } from '$lib/api/serverApiClient';
import { getLearningKitById } from '$lib/api/collections/learningKit';
import { handleApiError } from '$lib/api/apiError';


export const load = handleApiError(async ({ params }) => {
	const kit = await serverApiClient.req(getLearningKitById, null, params.learningKitId);

	return {
		kitToDisplay: kit
	};
});
