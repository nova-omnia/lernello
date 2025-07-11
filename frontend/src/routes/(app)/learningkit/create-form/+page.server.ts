import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail } from '@sveltejs/kit';
import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { createLearningKit } from '$lib/api/collections/learningKit';
import { api } from '$lib/api/apiClient';

export const load = async () => {
	const form = await superValidate(zod(CreateLearningKitSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request, fetch }) => {
		const form = await superValidate(request, zod(CreateLearningKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		await api(fetch).req(createLearningKit, form.data).parse();

		return { success: true, form };
	})
} satisfies Actions;
