import { getLocaleFromNavigator, locale, waitLocale } from 'svelte-i18n';

import { initi18n } from '$lib/i18n/i18n';
import { browser } from '$app/environment';

export const load = async ({ data }) => {
	if (browser) {
		const lang = data.userInfo?.locale || getLocaleFromNavigator();
		await initi18n(lang);
		locale.set(lang);
	}
	await waitLocale();
};
