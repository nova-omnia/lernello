import { parseLoginUser } from '$lib/models/LoginUser';
import { requestLoginUser } from '$lib/api/loginApi';
import type { Actions } from '@sveltejs/kit';
import { redirect } from '@sveltejs/kit';

export const actions: Actions = {
	loginUser: async ({ request }) => {
		const data = await request.formData();
		const user = parseLoginUser(data);

		await requestLoginUser(user);

		throw redirect(303, '/dashboard');
	}
};
