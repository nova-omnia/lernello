import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { handleApiError } from '$lib/api/apiError';
import { setError, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { loadUserInfo, parseRedirectTo, requireLogin } from '$lib/server/auth';
import { ChangePasswordDataSchema } from '$lib/schemas/request/ChangePasswordData';
import { serverApiClient } from '$lib/api/serverApiClient.js';
import { changePassword } from '$lib/api/collections/user';

export const load = async ({ url }) => {
	requireLogin();
	const userInfo = await loadUserInfo();

	if (userInfo.changedPassword) {
		const redirectTo = parseRedirectTo(url);
		redirect(303, redirectTo);
	}

	const form = await superValidate(zod(ChangePasswordDataSchema));
	return { form };
};

export const actions = {
	changePassword: handleApiError(async ({ request, url }) => {
		const form = await superValidate(request, zod(ChangePasswordDataSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const { success } = await serverApiClient.req(changePassword, form.data);
		if (!success) {
			return setError(form, 'confirmPassword', 'Change password failed');
		}

		const redirectTo = parseRedirectTo(url);
		console.log('redirectTo', redirectTo);

		redirect(303, redirectTo);
	})
} satisfies Actions;
