import { parseLoginUser } from '$lib/models/LoginUser';
import { requestLoginUser } from '$lib/api/loginApi';
import type { Actions } from '@sveltejs/kit';
import { fail, redirect } from '@sveltejs/kit';
import { ApiError, type ApiErrorResponse } from '$lib/api/ApiError';
import { getRequestEvent } from '$app/server';

export const actions = {
	loginUser: async ({ request }) => {
		try {
			const data = await request.formData();
			const user = parseLoginUser(data);

			const userToken = await requestLoginUser(user);
			const { locals } = getRequestEvent();
			locals.user = userToken;

			const url = new URL(request.url);
			const redirectTo = url.searchParams.get('redirectTo') || '/dashboard';
			redirect(307, redirectTo);
		} catch (error) {
			console.error('An unexpected error occurred:', error);
			if (error instanceof ApiError) {
				return fail(error.status, error.toApiErrorResponse());
			}
			return fail(500, {
				status: 500,
				message: 'An unexpected error occurred',
				error: 'Internal Server Error'
			} satisfies ApiErrorResponse);
		}
	}
} satisfies Actions;
