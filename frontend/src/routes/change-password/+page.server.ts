import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { setError, superValidate, type SuperValidated } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { ChangePasswordDataSchema } from '$lib/models/changePasswordData';
import { changePassword } from '$lib/api/login/changePassword';
import { requireLogin } from '$lib/server/auth';

export const load = async ({ request }) => {
	const user = requireLogin();

	//TODO changedPassword not updated
	if (user.changedPassword) {
		const redirectTo = new URL(request.url).searchParams.get('redirectTo') || '/dashboard';
		return redirect(303, redirectTo);
	}

	const form = await superValidate(zod(ChangePasswordDataSchema));
	return { form };
};

function handleApiFormError(
	form: SuperValidated<any>,
	error: ApiError,
	field: string = 'confirmPassword'
) {
	switch (error.status) {
		case 400:
			return setError(form, field, 'Invalid password');
		case 401:
			return setError(form, field, 'Unauthorized: Please log in');
		case 403:
			return setError(form, field, 'Not allowed, permission denied');
		default:
			return setError(form, field, error.message || 'An unknown error occurred');
	}
}

export const actions = {
	changePassword: handleApiError(async ({ request }) => {
		requireLogin();

		const form = await superValidate(request, zod(ChangePasswordDataSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		try {
			const { success } = await changePassword(form.data);
			if (!success) {
				return setError(form, 'confirmPassword', 'Change password failed');
			}
		} catch (error) {
			if (error instanceof ApiError) {
				return handleApiFormError(form, error);
			}
			throw error;
		}

		const redirectTo = new URL(request.url).searchParams.get('redirectTo') || '/dashboard';
		return redirect(303, redirectTo);
	})
} satisfies Actions;
