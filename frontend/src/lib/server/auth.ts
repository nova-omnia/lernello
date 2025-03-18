import { redirect } from '@sveltejs/kit';
import { getRequestEvent } from '$app/server';

export function requireLogin() {
	const { locals, url } = getRequestEvent();

	// assume `locals.user` is populated in `handle`
	if (!locals.user) {
		let redirectTo = url.pathname + url.search;
		redirectTo = redirectTo === '/' ? '/dashboard' : redirectTo;
		const params = new URLSearchParams({ redirectTo });

		redirect(307, `/login?${params}`);
	}

	return locals.user;
}
