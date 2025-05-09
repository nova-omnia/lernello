import { handleApiError } from '$lib/api/apiError.js';
import { getRequestEvent } from '$app/server';
import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
// import {api} from "$lib/api/apiClient"; // Not needed if not making API calls here
// import {getLearningUnitById} from "$lib/api/collections/learningUnit"; // Not needed

export const load = handleApiError(async ({ params }) => {
	// const { learningKit } = params; // Original, likely a typo, route is [learningkitId]
	const { learningkitId } = params; // Corrected to match route parameter

	// The API call for learning unit details was here, but it's not used by the page
	// and seems out of place for a learning kit statistics page.
	// const learning = await api(fetch).req(getLearningUnitById, null, learningUnitId).parse();

	const event = getRequestEvent();
	const role = event?.locals.userInfo?.role ?? TRAINEE_ROLE;

	return {
		role,
		learningkitId // Pass the learningkitId to the page
	};
});
