import { loadUserInfo } from '$lib/server/auth';
import { recoverSession } from '$lib/server/auth';
import type { Handle } from '@sveltejs/kit';

export const handle: Handle = async ({ event, resolve }) => {
	const tokenInfo = recoverSession();

	if (tokenInfo) {
		await loadUserInfo();
	}
	return resolve(event);
};
