import { redirect } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';
import { UserTokenSchema } from '$lib/models/user';

export function recoverSession() {
	const { locals, cookies } = getRequestEvent();
	// Recover session
	if (!locals.user) {
		const sessionToken = cookies.get('sessionToken');

		if (sessionToken) {
			try {
				const parsedToken = UserTokenSchema.parse(JSON.parse(sessionToken));
				locals.user = parsedToken;
			} catch (error) {
				console.error('Failed to parse session token:', error);
			}
		}
	}
}

export function requireLogin() {
	const { locals, url } = getRequestEvent();
	// try to recover session
	recoverSession();

	// assume `locals.user` is populated in `handle`
	if (!locals.user) {
		let redirectTo = url.pathname + url.search;
		redirectTo = redirectTo === '/' ? '/dashboard' : redirectTo;
		const params = new URLSearchParams({ redirectTo });

		redirect(307, `/login?${params}`);
	}

	return locals.user;
}
