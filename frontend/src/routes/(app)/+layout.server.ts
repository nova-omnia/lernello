import { loadUserInfo, parseRedirectTo, requireLogin } from '$lib/server/auth';
import { redirect } from '@sveltejs/kit';

export const load = async ({ url }) => {
	const refreshedToken = await requireLogin();

	const userInfo = await loadUserInfo();

	const isOnChangePWPage = url.pathname.startsWith('/change-password');

	if (!userInfo.changedPassword && !isOnChangePWPage) {
		const changePw = new URL('/change-password', url.origin);
		const redirectTo = parseRedirectTo(url, url.pathname + url.search);
		changePw.searchParams.set('redirectTo', redirectTo);
		redirect(303, changePw.toString());
	}

	return {
		refreshedToken
	};
};
