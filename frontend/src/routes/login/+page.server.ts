import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { setError, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { parseRedirectTo } from '$lib/server/auth';
import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { serverApiClient } from '$lib/api/serverApiClient';
import { signin } from '$lib/api/collections/auth';

export const load = async () => {
	const form = await superValidate(zod(UserLoginSchema));
	return { form };
};

export const actions = {
	login: handleApiError(async ({ request, cookies, url }) => {
		const form = await superValidate(request, zod(UserLoginSchema));
		if (!form.valid) {
			return fail(400, { form });
		}
		try {
			const loggedInUser = await serverApiClient.req(signin, form.data);
			cookies.set('sessionToken', JSON.stringify(loggedInUser), {
				httpOnly: true,
				path: '/',
				maxAge: loggedInUser.expires / 1000 // convert milliseconds to seconds
			});
			if (!loggedInUser.changedPassword) {
				cookies.set('shouldChangePw', 'true', {
					path: '/'
				});
			}

			const redirectTo = parseRedirectTo(url);

			redirect(303, redirectTo);
		} catch (error) {
			if (error instanceof ApiError) {
				if (error.status === 403 || error.error === 'Forbidden') {
					return setError(form, 'password', 'Invalid username or password');
				}
			}
			throw error;
		}
	})
} satisfies Actions;
