import { parseRedirectTo, requireLogin } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async ({ url, cookies }) => {
	requireLogin();

	const shouldChangePw = cookies.get('shouldChangePw');
	if (shouldChangePw) {
		const changePw = new URL('/change-password', url.origin);
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		changePw.searchParams.set('redirectTo', redirectTo);
		redirect(303, changePw.toString());
	}
};
