import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, error, fail, redirect } from '@sveltejs/kit';
import { api } from '$lib/api/apiClient';
import { UpdateLearningKitSchema } from '$lib/schemas/request/UpdateLearningKit';
import { getUserById, updateUser } from '$lib/api/collections/user';
import { UserResSchema } from '$lib/schemas/response/UserResSchema';

export const load = async ({ fetch, params }) => {
	if (!params.userId) {
		return error(400, 'LearningKit ID is required');
	}

	const user = await api(fetch).req(getUserById, null, params.userId).parse();
	const form = await superValidate(
		{
			username: user.username,
			name: user.name,
			surname: user.surname,
			role: user.role
		},
		zod(UserResSchema)
	);

	return { form };
};

export const actions = {
	update: handleApiError(async ({ request, fetch, params }) => {
		const form = await superValidate(request, zod(UpdateLearningKitSchema));
		if (!form.valid || !params.userId) {
			return fail(400, { form });
		}
		await api(fetch).req(updateUser, form.data, params.userId).parse();
		redirect(303, `/users`);
	})
} satisfies Actions;
