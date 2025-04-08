import { getLocaleFromNavigator, init, register } from 'svelte-i18n';

register('en-EN', () => import('./locales/en.json'));
register('de-DE', () => import('./locales/de.json'));
register('fr-FR', () => import('./locales/fr.json'));
register('it-IT', () => import('./locales/it.json'));

export async function initI18n(initialLocale: string | null) {
	await init({
		fallbackLocale: 'en-EN',
		initialLocale: initialLocale || getLocaleFromNavigator() || 'en-EN'
	});
}
