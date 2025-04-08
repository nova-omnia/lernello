import { redirect } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';
import { serverApiClient } from '$lib/api/serverApiClient';
import { getUserInfo } from '$lib/api/collections/user';

export function recoverAuthToken() {
	const { locals, cookies } = getRequestEvent();
	// Recover session
	if (!locals.tokenInfo) {
		const authToken = cookies.get('lernello_auth_token');

		if (authToken) {
			try {
				const parsedToken = LoggedInUserSchema.parse(JSON.parse(authToken));
				locals.tokenInfo = parsedToken;
			} catch (error) {
				console.error('Failed to parse session token:', error);
			}
		}
	}

	return locals.tokenInfo;
}
export async function loadUserInfo() {
	const isLoggedIn = recoverAuthToken();
	if (!isLoggedIn) {
		throw new Error('User is not authenticated!');
	}
	const { locals } = getRequestEvent();
	if (!locals.userInfo) {
		const userInfo = await serverApiClient.req(getUserInfo, null);
		locals.userInfo = userInfo;
	}
	return locals.userInfo;
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
