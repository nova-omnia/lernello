import { parseLoginUser } from '$lib/models/LoginUser';
import { requestLoginUser } from '$lib/api/loginApi';
import type { Actions } from '@sveltejs/kit';
import { redirect } from '@sveltejs/kit';

export const actions = {
	loginUser: async ({ request, cookies }) => {
		const data = await request.formData();
		const user = parseLoginUser(data);

		const userToken = await requestLoginUser(user);
		cookies.set('sessionToken', JSON.stringify(userToken), {
			httpOnly: true,
			path: '/',
			maxAge: userToken.expires / 1000 // convert milliseconds to seconds
		});

		const url = new URL(request.url);
		const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';
		redirect(303, redirectTo);
	}
} satisfies Actions;
