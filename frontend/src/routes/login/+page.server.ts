import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { message, setError, superValidate, type Infer } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { isLoggedIn, parseRedirectTo, setAuthCookies } from '$lib/server/auth';
import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { signin } from '$lib/api/collections/auth';
import { type LoggedInUserNoRefresh } from '$lib/schemas/response/LoggedInUser';
import { api } from '$lib/api/apiClient.js';

export const load = async ({ url }) => {
	if (isLoggedIn()) {
		redirect(303, parseRedirectTo(url));
	}

	const form = await superValidate<Infer<typeof UserLoginSchema>, Message>(zod(UserLoginSchema));

	return { form };
};

type Message = { redirectTo: string; tokenInfo: LoggedInUserNoRefresh };

export const actions = {
	login: handleApiError(async ({ request, cookies, url, fetch }) => {
		const form = await superValidate<Infer<typeof UserLoginSchema>, Message>(
			request,
			zod(UserLoginSchema)
		);
		if (!form.valid) {
			return fail(400, { form });
		}
		try {
			const loggedInUserRes = await api(fetch).req(signin, form.data, undefined).response;
			const loggedInUserResJson = await loggedInUserRes.json();
			const loggedInUser = signin.response.schema.parse(loggedInUserResJson);
			const loggedInUserNoRefresh = setAuthCookies(cookies, loggedInUser);
			return message(form, {
				redirectTo: parseRedirectTo(url),
				tokenInfo: loggedInUserNoRefresh
			});
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
