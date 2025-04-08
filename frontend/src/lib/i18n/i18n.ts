import { init, register } from 'svelte-i18n';

register('en', () => import('./locales/en.json'));
register('en', () => import('./locales/en.json'));
register('de', () => import('./locales/de.json'));
register('fr', () => import('./locales/fr.json'));
register('it', () => import('./locales/it.json'));

export async function initi18n(initialLocale?: string | null) {
	await init({
		fallbackLocale: 'de',
		initialLocale: initialLocale || 'en'
	});
}
