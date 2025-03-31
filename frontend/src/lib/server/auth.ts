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

	return locals.user;
}

export function requireLogin() {
	const { url } = getRequestEvent();
	// try to recover session
	const user = recoverSession();

	// assume `locals.user` is populated in `handle`
	if (!user) {
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		const params = new URLSearchParams({ redirectTo });

		redirect(307, `/login?${params}`);
	}

	return user;
}

export function parseRedirectTo(url: URL, fallback: string = '/dashboard') {
	const redirectTo = url.searchParams.get('redirectTo');
	if (redirectTo) {
		url.searchParams.delete('redirectTo');
		return redirectTo;
	}
	return fallback;
}
