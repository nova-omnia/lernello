import { loadUserInfo, recoverAuthToken } from '$lib/server/auth';
import type { Handle } from '@sveltejs/kit';

export const handle: Handle = async ({ event, resolve }) => {
	const tokenInfo = recoverAuthToken();

	if (tokenInfo) {
		await loadUserInfo();
	}
	return resolve(event);
};
