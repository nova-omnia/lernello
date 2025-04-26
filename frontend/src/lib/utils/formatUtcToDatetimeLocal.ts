export function formatUtcToDatetimeLocal(utcDateString: string | null | undefined): string | null {
	if (!utcDateString) return null;

	const date = new Date(utcDateString);
	if (isNaN(date.getTime())) return null;

	return (
		date.getFullYear() +
		'-' +
		String(date.getMonth() + 1).padStart(2, '0') +
		'-' +
		String(date.getDate()).padStart(2, '0') +
		'T' +
		String(date.getHours()).padStart(2, '0') +
		':' +
		String(date.getMinutes()).padStart(2, '0')
	);
}
