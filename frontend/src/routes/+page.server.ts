import { parseLoginUser } from '$lib/models/LoginUser';
import { requestLoginUser } from '$lib/api/loginApi';
import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, type ApiErrorResponse } from '$lib/api/ApiError';

export const actions = {
	loginUser: async ({ request }) => {
		try {
			const data = await request.formData();
			const user = parseLoginUser(data);

			await requestLoginUser(user);

			redirect(303, '/dashboard');
		} catch (error) {
			if (error instanceof ApiError) {
				return fail(error.status, error.toApiErrorResponse());
			}
			console.error('An unexpected error occurred:', error);
			return fail(500, {
				status: 500,
				message: 'An unexpected error occurred',
				error: 'Internal Server Error'
			} satisfies ApiErrorResponse);
		}
	}
} satisfies Actions;
