import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail } from '@sveltejs/kit';
import { api } from '$lib/api/apiClient';
import { CreateUserSchema } from '$lib/schemas/request/CreateUser';
import { createUser } from '$lib/api/collections/user';

export const load = async () => {
	const form = await superValidate(zod(CreateUserSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request, fetch }) => {
		const form = await superValidate(request, zod(CreateUserSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		await api(fetch).req(createUser, form.data).parse();

		return { success: true };
	})
} satisfies Actions;
