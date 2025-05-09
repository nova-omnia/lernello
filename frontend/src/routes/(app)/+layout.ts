import { browser } from '$app/environment';
import { setNewTokenData } from '$lib/api/apiClientAuthMiddleware';

export const load = async ({ data }) => {
	if (browser && data.refreshedToken) {
		setNewTokenData(data.refreshedToken);
	}

	return data;
};
