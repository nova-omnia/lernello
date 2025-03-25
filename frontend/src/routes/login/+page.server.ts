import { UserLoginSchema } from '$lib/models/user';
import { login } from '$lib/api/login/auth';
import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { setError, superValidate, type SuperValidated } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';

export const load = async () => {
	const form = await superValidate(zod(UserLoginSchema));
	return { form };
};

function handleApiFormError(
	form: SuperValidated<any>,
	error: ApiError,
	field: string = 'username'
) {
	switch (error.status) {
		case 400:
			return setError(form, field, 'Invalid username or password');
		case 401:
			return setError(form, field, 'Unauthorized: Please log in');
		case 403:
			return setError(form, field, 'Not allowed, permission denied');
		default:
			return setError(form, field, error.message || 'An unknown error occurred');
	}
}

export const actions = {
	login: handleApiError(async ({ request, cookies }) => {
		const form = await superValidate(request, zod(UserLoginSchema));
		if (!form.valid) {
			return fail(400, { form });
		}
		try {
			const loggedInUser = await login(form.data);
			cookies.set('sessionToken', JSON.stringify(loggedInUser), {
				httpOnly: true,
				path: '/',
				maxAge: loggedInUser.expires / 1000 // convert milliseconds to seconds
			});
			const url = new URL(request.url);
			const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';

			if (!loggedInUser.changedPassword) {
				redirect(303, `/change-password?redirectTo=${encodeURIComponent(redirectTo)}`);
			}
			redirect(303, redirectTo);
		} catch (error) {
			if (error instanceof ApiError) {
				return handleApiFormError(form, error, 'password');
			}
			throw error;
		}
	})
} satisfies Actions;
