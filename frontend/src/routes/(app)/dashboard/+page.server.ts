import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
import { handleApiError } from '$lib/api/apiError';
import { getRequestEvent } from '$app/server';

export const load = handleApiError(async () => {
	const role = getRequestEvent().locals.userInfo?.role ?? TRAINEE_ROLE;

	return {
		role
	};
});
