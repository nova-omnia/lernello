import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import {
	CreateLearningKitSchema,
	EditLearningKitSchema
} from '$lib/schemas/request/CreateLearningKit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { createLearningKit } from '$lib/api/collections/learningKit';

let editId: string | null;
export const load = async () => {
	let formData;

	const form = await superValidate(formData, zod(EditLearningKitSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateLearningKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		let learningKit = await serverApiClient.req(createLearningKit, {
			...form.data,
			deadlineDate: form.data.deadlineDate
				? form.data.deadlineDate.toISOString().split('T')[0]
				: null
		});

		return redirect(303, `/learningkit/${learningKit.uuid}`);
	})
} satisfies Actions;
