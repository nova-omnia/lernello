import { parseRedirectTo, requireLogin } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async ({ url, locals, parent }) => {
	await parent();
	const refreshedToken = locals.recoveredToken;
	requireLogin();

	if (refreshedToken) {
		locals.recoveredToken = undefined; // we have recovered it to return it to the client already
	}

	const isOnChangePWPage = url.pathname.startsWith('/change-password');

	if (!locals.userInfo?.changedPassword && !isOnChangePWPage) {
		const changePw = new URL('/change-password', url.origin);
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		changePw.searchParams.set('redirectTo', redirectTo);
		redirect(303, changePw.toString());
	}

	if (!locals.userInfo) {
		throw new Error('User is not authenticated!'); // should not happen, but TS needs to know userInfo is always defined
	}

	return {
		refreshedToken,
		userInfo: locals.userInfo
	};
};
