import { init, register } from 'svelte-i18n';

register('en-EN', () => import('./locales/en.json'));
register('de-DE', () => import('./locales/de.json'));
register('fr-FR', () => import('./locales/fr.json'));
register('it-IT', () => import('./locales/it.json'));

export function initi18n(initialLocale: string) {
	init({
		fallbackLocale: 'en-EN',
		initialLocale: initialLocale
	});
}
