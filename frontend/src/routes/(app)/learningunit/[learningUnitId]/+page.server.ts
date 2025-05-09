import { api } from '$lib/api/apiClient.js';
import { handleApiError } from '$lib/api/apiError.js';
import { getLearningUnitById } from '$lib/api/collections/learningUnit';
import { getRequestEvent } from '$app/server';
import { redirect } from '@sveltejs/kit';
import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

export const load = handleApiError(async ({ params, fetch }) => {
	const { learningUnitId } = params;

	const learningUnit = await api(fetch).req(getLearningUnitById, null, learningUnitId).parse();

	const role = getRequestEvent().locals.userInfo?.role ?? TRAINEE_ROLE;

	if (role === TRAINEE_ROLE) {
		throw redirect(308, `./${learningUnitId}/training`);
	}

	return {
		learningUnitId,
		learningUnit
	};
});
