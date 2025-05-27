import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { fail, redirect } from '@sveltejs/kit';
import { api } from '$lib/api/apiClient';
import { CreateTraineeUserSchema } from '$lib/schemas/request/user/CreateTraineeUser';
import { addTraineeInLearningKit } from '$lib/api/collections/learningKit';

export const load = async () => {
	const form = await superValidate(zod(CreateTraineeUserSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request, fetch, params }) => {
		const form = await superValidate(request, zod(CreateTraineeUserSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		await api(fetch).req(addTraineeInLearningKit, form.data, params.learningKitId).parse();

		redirect(303, `/learningkit/${params.learningKitId}?tab=trainees`);
	})
};
