import { serverApiClient } from '$lib/api/serverApiClient';
import { getLearningKitById } from '$lib/api/collections/learningKit';
import { handleApiError } from '$lib/api/apiError';
import { getAllTrainees } from '$lib/api/collections/user';

export const load = handleApiError(async ({ params }) => {
	const kit = await serverApiClient.req(getLearningKitById, null, params.learningKitId);
	const allTrainees = await serverApiClient.req(getAllTrainees, null);

	return {
		kitToDisplay: kit,
		allTrainees
	};
});
