import { loadUserInfo, parseRedirectTo, requireLogin } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async ({ url, locals }) => {
	requireLogin();
	const refreshedToken = locals.recoveredToken;
	if (refreshedToken) {
		locals.recoveredToken = undefined; // we have recovered it to return it to the client already
	}

	const userInfo = await loadUserInfo();

	const isOnChangePWPage = url.pathname.startsWith('/change-password');

	if (!userInfo.changedPassword && !isOnChangePWPage) {
		const changePw = new URL('/change-password', url.origin);
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		changePw.searchParams.set('redirectTo', redirectTo);
		redirect(303, changePw.toString());
	}

	return {
		refreshedToken,
		userInfo
	};
};
