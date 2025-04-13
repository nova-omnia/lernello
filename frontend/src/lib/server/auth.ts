import { redirect } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';
import { getUserInfo } from '$lib/api/collections/user';
import { api } from '$lib/api/apiClient';

export function isLoggedIn() {
	const { locals, cookies } = getRequestEvent();
	// Recover session
	if (locals.isLoggedIn === undefined) {
		if (cookies.get('lernello_auth_token')) {
			try {
				locals.isLoggedIn = true;
			} catch (error) {
				console.error('Failed to parse session token:', error);
			}
		} else {
			locals.isLoggedIn = false;
		}
	}

	return !!locals.isLoggedIn;
}
export async function loadUserInfo() {
	if (!isLoggedIn()) {
		throw new Error('User is not authenticated!');
	}
	const { locals, fetch } = getRequestEvent();
	if (!locals.userInfo) {
		const userInfo = await api(fetch).req(getUserInfo, null).parse();
		locals.userInfo = userInfo;
	}
	return locals.userInfo;
}

export function requireLogin() {
	const { url } = getRequestEvent();

	// assume `locals.user` is populated in `handle`
	if (!isLoggedIn()) {
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		const params = new URLSearchParams({ redirectTo });

		redirect(307, `/login?${params}`);
	}

	return isLoggedIn;
}

export function parseRedirectTo(url: URL, fallback: string = '/dashboard') {
	const redirectTo = url.searchParams.get('redirectTo');
	if (!redirectTo?.startsWith('/')) {
		console.error('Only relative redirects are allowed');
		return fallback;
	}

	if (redirectTo) {
		return redirectTo;
	}
	return fallback;
}
