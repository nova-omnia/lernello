export const load = ({ locals }) => {
	return {
		locale: locals.user?.locale
	};
};
