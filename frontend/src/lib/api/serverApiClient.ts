import { recoverAuthToken } from '$lib/server/auth';
import { createApiClient } from './createApiClient';

export const serverApiClient = createApiClient((request) => {
	const tokenInfo = recoverAuthToken();
	if (!tokenInfo) {
		throw new Error('User is not authenticated');
	}

	request.headers = {
		...request.headers,
		Authorization: `Bearer ${tokenInfo.token}`
	};

	return request;
});
