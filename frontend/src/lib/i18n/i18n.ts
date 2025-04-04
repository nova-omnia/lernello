import { getLocaleFromNavigator, init, register, waitLocale } from 'svelte-i18n';
import en from './locales/en.json';
import de from './locales/de.json';
import fr from './locales/fr.json';
import it from './locales/it.json';

register('en-EN', () => Promise.resolve(en));
register('de-DE', () => Promise.resolve(de));
register('fr-FR', () => Promise.resolve(fr));
register('it-IT', () => Promise.resolve(it));

const savedLocale = typeof localStorage !== 'undefined' ? localStorage.getItem('locale') : null;
const initialLocale = savedLocale || getLocaleFromNavigator();

init({
	fallbackLocale: 'it-IT',
	initialLocale
});

export const i18nReady = waitLocale();
