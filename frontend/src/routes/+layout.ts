import { getLocaleFromNavigator, locale, waitLocale } from 'svelte-i18n';
import { QueryClient } from '@tanstack/svelte-query';
import { initi18n } from '$lib/i18n/i18n';
import { browser } from '$app/environment';

export const load = async ({ data }) => {
	if (browser) {
		const lang = data.userInfo?.locale || getLocaleFromNavigator();
		await initi18n(lang);
		locale.set(lang);
	}
	await waitLocale();

	const queryClient = new QueryClient({
		defaultOptions: {
			queries: {
				enabled: browser,
				staleTime: 60 * 1000
			}
		}
	});

	return { queryClient };
};
