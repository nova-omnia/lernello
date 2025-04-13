import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { createLearningKit } from '$lib/api/collections/learningKit';

export const load = async () => {
	const form = await superValidate(zod(CreateLearningKitSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateLearningKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const learningKit = await serverApiClient.req(createLearningKit, form.data);

		return redirect(303, `/learningkit/${learningKit.uuid}`);
	})
} satisfies Actions;
