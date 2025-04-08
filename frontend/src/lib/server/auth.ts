import { redirect } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';

export function recoverAuthToken() {
	const { locals, cookies } = getRequestEvent();
	// Recover session
	if (!locals.user) {
		const authToken = cookies.get('lernello_auth_token');

		if (authToken) {
			try {
				const parsedToken = LoggedInUserSchema.parse(JSON.parse(authToken));
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
	const tokenInfo = recoverAuthToken();

	// assume `locals.user` is populated in `handle`
	if (!tokenInfo) {
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		const params = new URLSearchParams({ redirectTo });

		redirect(307, `/login?${params}`);
	}

	return tokenInfo;
}

export function parseRedirectTo(url: URL, fallback: string = '/dashboard') {
	const redirectTo = url.searchParams.get('redirectTo');
	if (redirectTo) {
		url.searchParams.delete('redirectTo');
		return redirectTo;
	}
	return fallback;
}
