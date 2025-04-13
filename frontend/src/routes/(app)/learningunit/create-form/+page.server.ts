import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import { CreateLearningUnitSchema } from '$lib/schemas/request/CreateLearningUnit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { createLearningUnit } from '$lib/api/collections/learningUnit';

export const load = async () => {
	const form = await superValidate(zod(CreateLearningUnitSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateLearningUnitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const learningUnit = await serverApiClient.req(createLearningUnit, form.data);

		return redirect(303, `/learningunit/${learningUnit.uuid}`);
	})
} satisfies Actions;
