import { getRequestEvent } from '$app/server';
import { recoverSession } from '$lib/server/auth';
import { createApiClient } from './createApiClient';

export const serverApiClient = createApiClient((request) => {
	recoverSession();
	const { locals } = getRequestEvent();
	if (!locals.user) {
		throw new Error('User is not authenticated');
	}

	request.headers = {
		...request.headers,
		Authorization: `Bearer ${locals.user.token}`
	};

	return request;
});
