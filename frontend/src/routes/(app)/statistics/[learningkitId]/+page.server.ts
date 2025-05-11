import { handleApiError } from '$lib/api/apiError.js';
import { getRequestEvent } from '$app/server';
import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

export const load = handleApiError(async ({ params }) => {
	const { learningkitId } = params;

	const event = getRequestEvent();
	const role = event?.locals.userInfo?.role ?? TRAINEE_ROLE;

	return {
		role,
		learningkitId
	};
});
