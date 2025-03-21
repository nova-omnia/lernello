import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { handleApiError } from '$lib/api/apiError';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { ChangePasswordDataSchema } from '$lib/models/changePasswordData';
import { changePassword } from '$lib/api/login/changePassword';

export const load = async () => {
	const form = await superValidate(zod(ChangePasswordDataSchema));
	return { form };
};

export const actions = {
	changePassword: handleApiError(async ({ request, cookies }) => {
		const form = await superValidate(request, zod(ChangePasswordDataSchema));
		if (!form.valid) {
			return fail(400, { form });
		}
		const changePasswordStatus = await changePassword(form.data);

		changePasswordStatus ? redirect(303, '/dashboard') : fail(400, { form });
	})
} satisfies Actions;
