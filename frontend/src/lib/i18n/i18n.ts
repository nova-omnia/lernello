import { getLocaleFromNavigator, init, register } from 'svelte-i18n';
import en from './locales/en.json';
import de from './locales/de.json';
import fr from './locales/fr.json';
import it from './locales/it.json';

register('en-EN', () => Promise.resolve(en));
register('de-DE', () => Promise.resolve(de));
register('fr-FR', () => Promise.resolve(fr));
register('it-IT', () => Promise.resolve(it));

export async function initI18n(clientLocale: string | undefined) {
	await init({
		fallbackLocale: 'en-EN',
		initialLocale: clientLocale || getLocaleFromNavigator()
	});
}
