import { signin } from './collections/auth';
import { createApiClient } from './createApiClient';

export const browserApiClient = createApiClient((request) => {
	const tokenInfoStorage = JSON.parse(localStorage.getItem('lernello_auth_token') || 'null');
	const tokenInfo = signin.response.schema.parse(tokenInfoStorage);

	request.headers = {
		...request.headers,
		Authorization: `Bearer ${tokenInfo.token}`
	};

	return request;
});
