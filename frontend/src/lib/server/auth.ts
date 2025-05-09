import { redirect, type Cookies } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';
import { getUserInfo } from '$lib/api/collections/user';
import { api, BASE_URL } from '$lib/api/apiClient';
import { LoggedInUserNoRefreshSchema, type LoggedInUser } from '$lib/schemas/response/LoggedInUser';
import { refreshToken } from '$lib/api/collections/auth';

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
}

export function parseRedirectTo(url: URL, fallback: string = '/dashboard') {
	const redirectTo = url.searchParams.get('redirectTo');
	if (redirectTo && !redirectTo.startsWith('/')) {
		return fallback;
	}

	if (redirectTo) {
		return redirectTo;
	}
	return fallback;
}

export async function recoverSession() {
	const { cookies, fetch } = getRequestEvent();
	const refreshTokenCookie = cookies.get('lernello_refresh_token');
	if (!refreshTokenCookie) {
		throw new Error('No refresh token found');
	}

	const refreshRes = await fetch(`${BASE_URL}${refreshToken.getPath()}`, {
		method: refreshToken.method,
		headers: {
			'Content-Type': 'application/json',
			Authorization: `Bearer ${refreshTokenCookie}`
		}
	});
	const refreshResJson = await refreshRes.json();
	const loggedInUser = refreshToken.response.schema.parse(refreshResJson);
	const loggedInUserNoRefresh = setAuthCookies(cookies, loggedInUser);

	return loggedInUserNoRefresh;
}

export function setAuthCookies(cookies: Cookies, loggedInUser: LoggedInUser) {
	const expiresDate = new Date(loggedInUser.expires);
	const expiresMs = expiresDate.getTime() - Date.now();
	const refreshExpiresDate = new Date(loggedInUser.refreshExpires);
	const refreshExpiresMs = refreshExpiresDate.getTime() - Date.now();
	if (expiresMs < 0) {
		throw new Error('Newly retrieved token is expired');
	}
	if (refreshExpiresMs < 0) {
		throw new Error('Newly retrieved refresh token is expired');
	}

	cookies.set('lernello_auth_token', loggedInUser.token, {
		httpOnly: true,
		path: '/',
		maxAge: Math.floor(expiresMs / 1000)
	});
	cookies.set('lernello_refresh_token', loggedInUser.refreshToken, {
		httpOnly: true,
		path: '/',
		maxAge: Math.floor(refreshExpiresMs / 1000)
	});

	return LoggedInUserNoRefreshSchema.parse(loggedInUser);
}
