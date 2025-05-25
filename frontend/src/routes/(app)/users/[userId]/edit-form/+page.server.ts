import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, error, fail } from '@sveltejs/kit';
import { api } from '$lib/api/apiClient';
import { getUser, updateUser } from '$lib/api/collections/user';
import { UpdateUserSchema } from '$lib/schemas/request/user/UpdateUser';

export const load = async ({ fetch, params }) => {
	if (!params.userId) {
		throw error(400, 'User ID is required');
	}

	const user = await api(fetch).req(getUser, null, params.userId).parse();
	const form = await superValidate(
		{
			name: user.name,
			surname: user.surname,
			role: user.role
		},
		zod(UpdateUserSchema)
	);

	return {
		form,
		username: user.username
	};
};

export const actions = {
	update: handleApiError(async ({ request, fetch, params }) => {
		const form = await superValidate(request, zod(UpdateUserSchema));

		if (!form.valid || !params.userId) {
			return fail(400, { form });
		}

		await api(fetch).req(updateUser, form.data, params.userId).parse();
		return { success: true };
	})
} satisfies Actions;
