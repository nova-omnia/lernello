import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { setError, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { ChangePasswordDataSchema } from '$lib/models/changePasswordData';
import { changePassword } from '$lib/api/login/changePassword';
import { requireLogin } from '$lib/server/auth';

export const load = async () => {
	const form = await superValidate(zod(ChangePasswordDataSchema));
	return { form };
};

export const actions = {
	changePassword: handleApiError(async ({ request }) => {
		requireLogin();
		const form = await superValidate(request, zod(ChangePasswordDataSchema));
		if (!form.valid) {
			return fail(400, { form });
		}
		try {
			const changePasswordStatus = await changePassword(form.data);
			const url = new URL(request.url);
			const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';
			if (changePasswordStatus.success) {
				return redirect(303, redirectTo);
			}
		} catch (error) {
			if (error instanceof ApiError) {
				if (error.status === 403) {
					return setError(form, 'newPassword', 'Not allowed to change password');
				}
			}
			throw error;
		}
	})
} satisfies Actions;
