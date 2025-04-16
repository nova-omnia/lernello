import { getLearningKitById } from '$lib/api/collections/learningKit';
import { handleApiError } from '$lib/api/apiError';
import { getAllTrainees } from '$lib/api/collections/user';
import { getAllFiles } from '$lib/api/collections/file.js';
import { api } from '$lib/api/apiClient.js';

export const load = handleApiError(async ({ params, fetch }) => {
	const kit = await api(fetch).req(getLearningKitById, null, params.learningKitId).parse();
	const allTrainees = await api(fetch).req(getAllTrainees, null).parse();
	const allFiles = await api(fetch).req(getAllFiles, null).parse();

	return {
		kitToDisplay: kit,
		allTrainees,
		allFiles
	};
});
