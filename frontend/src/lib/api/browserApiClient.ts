import { createApiClient } from './createApiClient';

export const browserApiClient = createApiClient((request) => {
	request.credentials = 'include';
	return request;
});
