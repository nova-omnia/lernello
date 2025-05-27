import { init, register, getLocaleFromNavigator } from 'svelte-i18n';

register('en', () => import('./locales/en.json'));
register('de', () => import('./locales/de.json'));
register('fr', () => import('./locales/fr.json'));
register('it', () => import('./locales/it.json'));

function normalizeLocale(locale: string | null | undefined): string {
	if (!locale) return 'de';
	return locale.split(',')[0].split(';')[0].trim();
}

export async function initi18n(initialLocale?: string | null) {
	const locale = normalizeLocale(initialLocale || getLocaleFromNavigator());
	await init({
		fallbackLocale: 'de',
		initialLocale: locale
	});
}
