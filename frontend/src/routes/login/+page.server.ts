import type { Actions } from '@sveltejs/kit';
import { fail } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { message, setError, superValidate, type Infer } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { parseRedirectTo, recoverSession } from '$lib/server/auth';
import { UserLoginSchema } from '$lib/schemas/request/UserLogin';
import { publicApiClient } from '$lib/api/publicApiClient';
import { signin } from '$lib/api/collections/auth';
import type { LoggedInUser } from '$lib/schemas/response/LoggedInUser';

export const load = async () => {
	const form = await superValidate<Infer<typeof UserLoginSchema>, Message>(zod(UserLoginSchema));
	const user = recoverSession();
	if (user) {
		message(form, {
			redirectTo: '/',
			user
		});
	}
	return { form };
};

type Message = { redirectTo: string; user: LoggedInUser };

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
			const loggedInUser = signin.response.schema.parse(await loggedInUserRes.json());

			cookies.set('sessionToken', JSON.stringify(loggedInUser), {
				httpOnly: true,
				path: '/',
				maxAge: loggedInUser.expires / 1000 // convert milliseconds to seconds
			});

			return message(form, {
				redirectTo: parseRedirectTo(url),
				user: loggedInUser
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
