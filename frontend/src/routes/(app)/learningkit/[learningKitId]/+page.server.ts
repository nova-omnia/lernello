import { getRequestEvent } from '$app/server';
import { TRAINEE_ROLE } from '$lib/schemas/response/UserInfo';

export const load = async ({ url }) => {
	const tab = url.searchParams.get('tab') || '';

	const role = getRequestEvent().locals.userInfo?.role ?? TRAINEE_ROLE;

	return {
		tab,
		role
	};
};
