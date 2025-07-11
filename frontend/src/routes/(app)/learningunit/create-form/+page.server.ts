import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail } from '@sveltejs/kit';
import { CreateLearningUnitSchema } from '$lib/schemas/request/CreateLearningUnit';
import { createLearningUnit } from '$lib/api/collections/learningUnit';
import { api } from '$lib/api/apiClient';

export const load = async ({ url }) => {
	const learningKitId = url.searchParams.get('learningKitId') ?? '';
	const form = await superValidate(zod(CreateLearningUnitSchema));
	form.data.learningKitId = learningKitId;
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request, fetch }) => {
		const form = await superValidate(request, zod(CreateLearningUnitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		await api(fetch).req(createLearningUnit, form.data).parse();

		return { success: true, form };
	})
} satisfies Actions;
