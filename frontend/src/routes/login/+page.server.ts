import { UserLoginSchema } from '$lib/models/user';
import { login } from '$lib/api/auth';
import type { Actions } from '@sveltejs/kit';
import { redirect } from '@sveltejs/kit';
import { handleApiError } from '$lib/api/apiError';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { fail } from '@sveltejs/kit';

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

		const userToken = await login(form.data);
		cookies.set('sessionToken', JSON.stringify(userToken), {
			httpOnly: true,
			path: '/',
			maxAge: userToken.expires / 1000 // convert milliseconds to seconds
		});

		const url = new URL(request.url);
		const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';
		redirect(303, redirectTo);
	})
} satisfies Actions;
