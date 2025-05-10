export const load = async ({ url }) => {
	const tab = url.searchParams.get('tab') || '';

	return {
		tab
	};
};
