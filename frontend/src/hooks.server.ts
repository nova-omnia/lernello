import { initi18n } from '$lib/i18n/i18n';
import { loadUserInfo, recoverAuthToken } from '$lib/server/auth';
import type { Handle } from '@sveltejs/kit';
import { locale } from 'svelte-i18n';

export const handle: Handle = async ({ event, resolve }) => {
	const tokenInfo = recoverAuthToken();

	if (tokenInfo) {
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
