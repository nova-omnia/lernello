import { UserLoginSchema } from '$lib/models/user';
import { login } from '$lib/api/login/auth';
import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { handleApiError } from '$lib/api/apiError';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';

export const load = async () => {
	const form = await superValidate(zod(UserLoginSchema));
	return { form };
};

export const actions = {
	login: handleApiError(async ({ request, cookies }) => {
		const form = await superValidate(request, zod(UserLoginSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const loggedInUser = await login(form.data);
		if (!loggedInUser.changedPassword) {
			redirect(303, '/change-password');
		}

		cookies.set('sessionToken', JSON.stringify(loggedInUser), {
			httpOnly: true,
			path: '/',
			maxAge: loggedInUser.expires / 1000 // convert milliseconds to seconds
		});

		const url = new URL(request.url);

		const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';
		redirect(303, redirectTo);
	})
} satisfies Actions;
