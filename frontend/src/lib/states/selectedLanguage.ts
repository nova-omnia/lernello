let language = 'ENGLISH';

export function setSelectedLanguage(lang: string) {
	language = lang;
}

export function getSelectedLanguage(): string {
	return language;
}