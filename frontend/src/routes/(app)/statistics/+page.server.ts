import { handleApiError } from '$lib/api/apiError.js';
import { getRequestEvent } from '$app/server';
import { INSTRUCTOR_ROLE, TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';
import { redirect } from '@sveltejs/kit';

export const load = handleApiError(async () => {
	const role = getRequestEvent().locals.userInfo?.role ?? TRAINEE_ROLE;

	if (role !== INSTRUCTOR_ROLE) {
		redirect(307, '/dashboard');
	}
});
