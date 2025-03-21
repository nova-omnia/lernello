import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { handleApiError } from '$lib/api/apiError';
import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import {
	ChangedPasswordUser,
	ChangePasswordData,
	ChangePasswordDataSchema
} from '$lib/models/changePasswordData';
import { changePassword } from '$lib/api/login/changePassword';
import { getUsernameFromCookie } from '$lib/utility/sessionCookieUtils';

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
		const changePasswordData = ChangePasswordDataSchema.parse(form.data);
		if (validateNewPassword(changePasswordData)) {
			return fail(400, { form });
		}
		//TODO what if fail(401) is returned?
		const changePasswordUser = new ChangedPasswordUser(
			getUsernameFromCookie(cookies),
			changePasswordData.oldPassword,
			changePasswordData.newPassword,
			changePasswordData.confirmPassword
		);
		const changePasswordStatus = await changePassword(changePasswordUser);
		changePasswordStatus.success ? redirect(303, '/dashboard') : fail(400, { form });
	})
} satisfies Actions;

function validateNewPassword(changePassword: ChangePasswordData) {
	return changePassword.newPassword !== changePassword.confirmPassword;
}
