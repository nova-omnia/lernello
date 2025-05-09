import { browser } from '$app/environment';

export const load = async ({ data }) => {
	if (browser && data.refreshedToken) {
		localStorage.setItem('lernello_auth_token', JSON.stringify(data.refreshedToken));
	}

	return data;
};
