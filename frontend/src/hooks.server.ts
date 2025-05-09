import { BASE_URL } from '$lib/api/apiClient';
import { initi18n } from '$lib/i18n/i18n';
import { isLoggedIn, recoverSession } from '$lib/server/auth';
import type { Handle, HandleFetch } from '@sveltejs/kit';
import { locale } from 'svelte-i18n';

export const handle: Handle = async ({ event, resolve }) => {
	const refreshTokenCookie = event.cookies.get('lernello_refresh_token');
	if (!isLoggedIn() && refreshTokenCookie) {
		const freshTokens = await recoverSession();
		event.locals.recoveredToken = freshTokens;
		event.locals.isLoggedIn = true;
	}

	const lang =
		event.locals.userInfo?.locale || event.request.headers.get('accept-language')?.split(',')[0];

	if (lang) {
		await initi18n(lang);
		locale.set(lang);
	}

	return resolve(event);
};

export const handleFetch: HandleFetch = async ({ request, fetch, event }) => {
	if (request.url.startsWith(BASE_URL)) {
		request.headers.set('cookie', event.request.headers.get('cookie')!);
	}

	return fetch(request);
};
