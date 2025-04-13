import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { message, setError, superValidate, type Infer } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { parseRedirectTo, isLoggedIn } from '$lib/server/auth';
import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { publicApiClient } from '$lib/api/publicApiClient';
import { signin } from '$lib/api/collections/auth';
import type { LoggedInUser } from '$lib/schemas/response/LoggedInUser';

export const load = async ({ url }) => {
	const form = await superValidate<Infer<typeof UserLoginSchema>, Message>(zod(UserLoginSchema));

	if (isLoggedIn()) {
		redirect(303, parseRedirectTo(url));
	}
	return { form };
};

type Message = { redirectTo: string; tokenInfo: LoggedInUser };

export const actions = {
	login: handleApiError(async ({ request, cookies, url }) => {
		const form = await superValidate<Infer<typeof UserLoginSchema>, Message>(
			request,
			zod(UserLoginSchema)
		);
		if (!form.valid) {
			return fail(400, { form });
		}
		try {
			const loggedInUserRes = await publicApiClient.reqRaw(signin, form.data, undefined);
			const loggedInUserResJson = await loggedInUserRes.json();
			const loggedInUser = signin.response.schema.parse(loggedInUserResJson);
			const expiresDate = new Date(loggedInUser.expires);
			const expiresMs = expiresDate.getTime() - Date.now();
			if (expiresMs < 0) {
				throw new Error('Newly retrieved token is expired');
			}

			cookies.set('lernello_auth_token', loggedInUser.token, {
				httpOnly: true,
				path: '/',
				maxAge: Math.floor(expiresMs / 1000)
			});

			return message(form, {
				redirectTo: parseRedirectTo(url),
				tokenInfo: loggedInUser
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
