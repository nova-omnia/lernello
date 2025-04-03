import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { setError, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { parseRedirectTo, requireLogin } from '$lib/server/auth';
import { ChangePasswordDataSchema } from '$lib/schemas/request/ChangePasswordData';
import { serverApiClient } from '$lib/api/serverApiClient.js';
import { changePassword } from '$lib/api/collections/user';

export const load = async ({ url }) => {
	const user = requireLogin();

	if (user.changedPassword) {
		const redirectTo = parseRedirectTo(url);
		redirect(303, redirectTo);
	}

	const form = await superValidate(zod(ChangePasswordDataSchema));
	return { form };
};

export const actions = {
	changePassword: handleApiError(async ({ request, url, cookies }) => {
		requireLogin();

		const form = await superValidate(request, zod(ChangePasswordDataSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		try {
			const { success } = await serverApiClient.req(changePassword, form.data);
			if (!success) {
				return setError(form, 'confirmPassword', 'Change password failed');
			}
			cookies.delete('shouldChangePw', {
				path: '/'
			});
		} catch (error) {
			if (error instanceof ApiError) {
				// switch (error.status) {
				// 	case 400:
				// 		return setError(form, 'newPassword', 'Invalid password');
				// 	case 401:
				// 		return setError(form, 'confirmPassword', 'Unauthorized: Please log in');
				// 	case 403:
				// 		return setError(form, 'confirmPassword', 'Not allowed, permission denied');
				// 	default:
				// 		return setError(form, 'confirmPassword', error.message || 'An unknown error occurred');
				// }
			}
			throw error;
		}

		const redirectTo = parseRedirectTo(url);
		console.log('redirectTo', redirectTo);

		redirect(303, redirectTo);
	})
} satisfies Actions;
