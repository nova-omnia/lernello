import { initi18n } from '$lib/i18n/i18n';
import { loadUserInfo, isLoggedIn } from '$lib/server/auth';
import type { Handle, HandleFetch } from '@sveltejs/kit';
import { locale } from 'svelte-i18n';

export const handle: Handle = async ({ event, resolve }) => {
	if (isLoggedIn()) {
		await loadUserInfo();
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
	if (request.url.startsWith('http://localhost:8080/')) {
		request.headers.set('cookie', event.request.headers.get('cookie')!);
	}

	return fetch(request);
};
