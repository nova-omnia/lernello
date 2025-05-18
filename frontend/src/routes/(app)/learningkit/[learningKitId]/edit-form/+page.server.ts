import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, error, fail } from '@sveltejs/kit';
import { getLearningKitById, updateLearningKit } from '$lib/api/collections/learningKit';
import { api } from '$lib/api/apiClient';
import { UpdateLearningKitSchema } from '$lib/schemas/request/UpdateLearningKit';
import { formatUtcToDatetimeLocal } from '$lib/utils/formatUtcToDatetimeLocal';

export const load = async ({ fetch, params }) => {
	if (!params.learningKitId) {
		return error(400, 'LearningKit ID is required');
	}

	const learningKit = await api(fetch).req(getLearningKitById, null, params.learningKitId).parse();
	const form = await superValidate(
		{
			name: learningKit.name ?? '',
			description: learningKit.description ?? '',
			deadlineDate: learningKit.deadlineDate ?? null
		},
		zod(UpdateLearningKitSchema)
	);
	form.data.deadlineDate = formatUtcToDatetimeLocal(form.data.deadlineDate);

	return { form };
};

export const actions = {
	update: handleApiError(async ({ request, fetch, params }) => {
		const form = await superValidate(request, zod(UpdateLearningKitSchema));
		if (!form.valid || !params.learningKitId) {
			return fail(400, { form });
		}

		await api(fetch).req(updateLearningKit, form.data, params.learningKitId).parse();

		return { form, success: true };
	})
} satisfies Actions;
