import { getLocaleFromNavigator, waitLocale } from 'svelte-i18n';

import { initi18n } from '$lib/i18n/i18n';

export const load = async ({ data }) => {
	initi18n(data.userInfo?.locale || getLocaleFromNavigator() || 'en-EN');

	await waitLocale();
};
